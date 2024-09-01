package com.giansiccardi.services;

import com.giansiccardi.dto.PaymentResponse;
import com.giansiccardi.enums.PaymentMethod;
import com.giansiccardi.enums.PaymentOrderStatus;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.PaymentOrder;
import com.giansiccardi.repository.PaymentOrderRepository;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;


import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentOrderService {

private final PaymentOrderRepository paymentOrderRepository;

@Value("${stripe.secret.key}")
private String stripeSecretKey;




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

public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws StripeException {

    log.info("ENTRANDO EN EL METODO PROCEEDPAYMENTORDER");
try {
    if (paymentOrder.getStatus() == null) {
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
    }

    if (paymentOrder.getStatus().equals((PaymentOrderStatus.PENDING))) {



        if (paymentOrder.getPaymentMethod().equals((PaymentMethod.STRIPE))) {

            paymentOrder.setStatus(PaymentOrderStatus.SUCCES);
            paymentOrderRepository.save(paymentOrder);
            return true;
        } else {

            paymentOrder.setStatus(PaymentOrderStatus.FAILED); // Considera agregar un estado FAILED si no es exitoso
            paymentOrderRepository.save(paymentOrder);
        }
    } else {
        log.info("PaymentOrder status is not PENDING: {}", paymentOrder.getStatus());
        paymentOrder.setStatus(PaymentOrderStatus.SUCCES);
        paymentOrderRepository.save(paymentOrder);
        return true;
    }

}catch (Exception e) {
    log.error("Unexpected error occurred: {}", e.getMessage());
}
return false;
}



    public PaymentResponse createStripePaymentLink(Customer customer, Long amount,Long orderId) throws StripeException {
        Stripe.apiKey=stripeSecretKey;


        long amountInCents = amount * 100;

        //  Construir los parámetros de la sesión de Stripe
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD) // Especifica el método de pago, en este caso, tarjeta de crédito (CARD).
                .setMode(SessionCreateParams.Mode.PAYMENT) // Define el modo de la sesión, está en modo PAYMENT, que significa que se espera un único pago.
                .setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId +  "&payment_id={CHECKOUT_SESSION_ID}") // La URL a la que se redirige al usuario después de un pago exitoso. Se incluye el orderId para identificar el pedido.
                .setCancelUrl("http://localhost:8080/payment/cancel") // La URL a la que se redirige al usuario si cancela el proceso de pago.
                .addLineItem(SessionCreateParams.LineItem.builder() // Define los artículos que el usuario va a pagar, incluyendo la cantidad, el precio, la moneda, y el nombre del producto.
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Transfiere a tu billetera")
                                        .build())
                                .setUnitAmount(amountInCents) // Aquí defines el precio en centavos.
                                .build())
                        .build())
                .build();

        // Crear la sesión de Stripe
        //  Llamo al método create de Stripe con los parámetros definidos para crear una
        //   sesión de pago. La sesión de pago incluye toda la información necesaria para procesar la transacción.
        Session session=Session.create(params);


        // Preparar la respuesta para el cliente
        System.out.printf("session___" , session);
        PaymentResponse response= new PaymentResponse();
        response.setPayment_url(session.getUrl());
        return response;
    }


}

