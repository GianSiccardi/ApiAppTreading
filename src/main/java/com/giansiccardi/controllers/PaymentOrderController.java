package com.giansiccardi.controllers;


import com.giansiccardi.dto.PaymentResponse;
import com.giansiccardi.enums.PaymentMethod;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.PaymentOrder;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.PaymentOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentOrderController {

private final CustomerServices customerServices;

private final PaymentOrderService paymentOrderService;




// El usuario  solicita un deposito ,en este enpoint creo una orden de pago y genero un link
//Entonces se devuelve payment_url donde el usuario ingresa para hacer el pago
//Por  ultimo stripe genera un payment_id que es el identificador de la transaccion

//Después de que el pago es exitoso, necesito confirmar que el pago se realizó correctamente y
// depositar la plata en la billetera del usuario. Aquí es donde entra el método addMoneyToWallet de WalletController.
@PostMapping("/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse>paymenHandler(
        @PathVariable PaymentMethod paymentMethod,
        @PathVariable Long amount,
        @RequestHeader("Authorization")String jwt
) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);

    PaymentResponse paymentResponse;

    PaymentOrder order= paymentOrderService.createOrder(customer,amount,paymentMethod);

    paymentResponse=paymentOrderService.createStripePaymentLink(customer,amount, order.getId());

    return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
}
}
