package com.giansiccardi.services;

import com.giansiccardi.dto.PaymentResponse;
import com.giansiccardi.enums.PaymentMethod;
import com.giansiccardi.enums.PaymentOrderStatus;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.PaymentOrder;
import com.giansiccardi.repository.PaymentOrderRepository;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentOrderService {

private final PaymentOrderRepository paymentOrderRepository;

@Value("${stripe.api.key}")
private String stripeSecretKey;

@Value("${razorpay.api.key}")
private String apiKey;

@Value("${razorpay.api.secret}")
private String apiSecretKey;

public PaymentOrder createOrder(Customer customer, Long amount, PaymentMethod paymentMethod){
PaymentOrder paymentOrder= new PaymentOrder();
paymentOrder.setCustomer(customer);
paymentOrder.setAmount(amount);
paymentOrder.setPaymentMethod(paymentMethod);
paymentOrder.setStatus(PaymentOrderStatus.PENDING);

return paymentOrderRepository.save(paymentOrder);
}

public PaymentOrder getPaymentOrderById(Long id) throws Exception {
return paymentOrderRepository.findById(id).orElseThrow(()->new Exception("orden no encontrada"));
}

public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException {
if(paymentOrder.getStatus()==null){
    paymentOrder.setStatus(PaymentOrderStatus.PENDING);
}

    if(paymentOrder.getStatus().equals((PaymentOrderStatus.PENDING))){
    if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
        RazorpayClient razorpayClient= new RazorpayClient(apiKey,apiSecretKey);
        Payment payment=razorpayClient.payments.fetch(paymentId);
        Integer amount=payment.get("amount");
        String status=payment.get("status");

        if(status.equals("captured")){
            paymentOrder.setStatus(PaymentOrderStatus.SUCCES);
            return true;
        }
        paymentOrder.setStatus(PaymentOrderStatus.FAILED);
        paymentOrderRepository.save(paymentOrder);
        return false;
    }
    paymentOrder.setStatus(PaymentOrderStatus.SUCCES);
    paymentOrderRepository.save(paymentOrder);
    return true;
}
return false;
}

public PaymentResponse createPaypalPaymentLink(Customer customer, Long amount){
Long Amount=amount*100;
try{
    RazorpayClient razorpayClient= new RazorpayClient(apiKey,apiSecretKey);
    //creamos el json con los parametors del link de pago

    JSONObject paymentLinkRequest= new JSONObject();
    paymentLinkRequest.put("amount",amount);
    paymentLinkRequest.put("currency","INR");

    //CREAR EL JSON CON LOS DETALLES DEL CUSTOMER

    JSONObject customerDetails= new JSONObject();
    customerDetails.put("name",customer.getFullName());
    customerDetails.put("email",customer.getEmail());
    paymentLinkRequest.put("customerDetails",customerDetails);

    //Crear JSON de notifiacion

    JSONObject notify = new JSONObject();
    notify.put("email",true);
    paymentLinkRequest.put("notify",notify);

    paymentLinkRequest.put("reminder_enable",true);

    paymentLinkRequest.put("callback_url","http://localhost:8080/wallet");
    paymentLinkRequest.put("callback_method","get");

    //Crear el link de pago para usar en paymentLink.create()
    PaymentLink payment=razorpayClient.paymentLink.create(paymentLinkRequest);

    String paymentLinkId=payment.get("id");
    String paymentLinkUrl=payment.get("short_url");

    PaymentResponse response=new PaymentResponse();

    response.setPayment_url(paymentLinkUrl);

    return response;
} catch (RazorpayException e) {
    throw new RuntimeException(e);
}

}

    public PaymentResponse createStripePaymentLink(Customer customer, Long amount,Long orderId) throws StripeException {
        Stripe.apiKey=stripeSecretKey;

        SessionCreateParams params=SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:8080/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Top up wallet")
                                        .build()
                                ).build()
                        ).build()
                ).build();

        Session session=Session.create(params);

        System.out.printf("session___" + session);
        PaymentResponse response= new PaymentResponse();
        response.setPayment_url(session.getUrl());
        return response;
    }
}
