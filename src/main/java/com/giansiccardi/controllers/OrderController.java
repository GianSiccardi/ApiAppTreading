package com.giansiccardi.controllers;

import com.giansiccardi.dto.CreateOrderRequest;
import com.giansiccardi.enums.OrderType;
import com.giansiccardi.models.Coin;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.Order;
import com.giansiccardi.services.CoinService;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;
    private final CustomerServices customerServices;
    private final CoinService coinService;



@PostMapping("/pay")
public ResponseEntity<Order>payOrderPayment(
        @RequestHeader("Authorization")String jwt,
        @RequestBody CreateOrderRequest request
        ) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);
    Coin coin =coinService.findById(request.getCoinId());
    Order order=orderService.processOrder(coin, request.getQuantity(), request.getOrderType(),customer);
    return ResponseEntity.ok().body(order);
}




@GetMapping("/{orderId}")
public ResponseEntity<Order>getOrderById(
        @RequestHeader("Authorization") String jwtToken,
        @PathVariable Long orderId
) throws Exception {
    if(jwtToken==null){
        throw new RuntimeException("Falta el token");

    }
    Customer customer=customerServices.findCustomerByJwt(jwtToken);
    Order order=orderService.getOrderById(orderId);
    if(order.getCustomer().equals(customer.getId())){
        return ResponseEntity.ok(order);
    }else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}


@GetMapping
public ResponseEntity<List<Order>>getAllOrdersForUser(
        @RequestHeader("Authorization") String jwtToken,
        @RequestParam (required = false) OrderType order_type,
        @RequestParam (required = false)String asseet_symbol
) throws Exception {
 if(jwtToken==null){
     throw new RuntimeException("Falta el token");
 }

 Long customerId=customerServices.findCustomerByJwt(jwtToken).getId();
List<Order>customerOrders=orderService.getAllOrdersOfCustomer(customerId,order_type,asseet_symbol);

    return ResponseEntity.ok(customerOrders);


}


}
