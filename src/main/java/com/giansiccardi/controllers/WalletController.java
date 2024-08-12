package com.giansiccardi.controllers;

import com.giansiccardi.dto.PaymentResponse;
import com.giansiccardi.dto.WalletTransaction;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.Order;
import com.giansiccardi.models.PaymentOrder;
import com.giansiccardi.models.Wallet;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.OrderService;
import com.giansiccardi.services.PaymentOrderService;
import com.giansiccardi.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final OrderService orderService;
    private PaymentOrderService paymentOrderService;

    private final CustomerServices customerServices;
    @GetMapping
    public ResponseEntity<Wallet>getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        Customer customer=customerServices.findCustomerByJwt(jwt);

        Wallet wallet=walletService.getUserWallet(customer);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }


    @PutMapping("/{order_id}/transfer")
    public ResponseEntity<Wallet>walletToWalletTransfer(@RequestHeader("Authorization")
                                                        String jwt,
                                                        @PathVariable  Long order_id,
                                                        @RequestBody WalletTransaction walletTransaction) throws Exception {

   Customer customerSender=customerServices.findCustomerByJwt(jwt);
   Wallet receiverWallet=walletService.findById(order_id);
   Wallet wallet=walletService.walletToWalletTransfe(customerSender,receiverWallet,walletTransaction.getAmount());
   return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);

    }


    @PutMapping("/order/{order_id}/pay")
    public ResponseEntity<Wallet>payOrderPayment(@RequestHeader("Authorization")
                                                        String jwt,
                                                        @PathVariable  Long order_id) throws Exception {

        Customer customer=customerServices.findCustomerByJwt(jwt);
        Order order=orderService.getOrderById(order_id);
        Wallet wallet=walletService.payOrderPayment(order,customer);
        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);

    }

    @PutMapping("/deposit")
    public ResponseEntity<Wallet>addMoneyToWallet(@RequestHeader("Authorization")
                                                 String jwt,
                                                 @RequestParam(name="order_id")Long order_id,
                                                 @RequestParam(name="payment_id")String payment_id
                                                ) throws Exception {

        Customer customer=customerServices.findCustomerByJwt(jwt);

        Wallet wallet=walletService.getUserWallet(customer);
        PaymentOrder order=paymentOrderService.getPaymentOrderById(order_id);
        Boolean status=paymentOrderService.ProceedPaymentOrder(order,payment_id);
   if(wallet.getBalance()==null){
       wallet.setBalance(BigDecimal.valueOf(0));
   }
      if(status){
          wallet=walletService.addBalance(wallet,order.getAmount());
      }
      

        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);

    }

}
