package com.giansiccardi.controllers;

import com.giansiccardi.dto.WalletTransaction;
import com.giansiccardi.enums.WalletTransactionType;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.Wallet;
import com.giansiccardi.models.Withdrawal;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.WalletService;
import com.giansiccardi.services.WithdrawalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/withdrawal")
@RequiredArgsConstructor
public class WithdrawalContoller {

    private final WithdrawalService withdrawalService;
    private final WalletService walletService;
    private final CustomerServices customerServices;

    
 //   private final WalletTransactionService walletTransactionService;

@PostMapping("/withdrawal/{amount}")
    public ResponseEntity<?>withdrawalRequest(
    @PathVariable Long amount,
    @RequestHeader("Authorization")String jwt
) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);
    Wallet customerWallet=walletService.getUserWallet(customer);

    Withdrawal withdrawal=withdrawalService.requestWithdrawal(amount,customer);
    walletService.addBalance(customerWallet,-withdrawal.getAmount());
  /*  WalletTransaction walletTransaction =walletTransactionService.createTransaction(
            customerWallet,
            WalletTransactionType.ADD_MONEY,null,"retiro de cuenta bancaria",withdrawal.getAmount());
    */
    return ResponseEntity.ok().body(withdrawal);

}
@PatchMapping("/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?>proceedWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization")String jwt) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);
    Withdrawal withdrawal= withdrawalService.procedWithdrawal(id,accept);
    Wallet customerWallet=walletService.getUserWallet(customer);
    if(!accept){
        walletService.addBalance(customerWallet,withdrawal.getAmount());
    }
    return ResponseEntity.ok().body(withdrawal);
}


@GetMapping()
    public ResponseEntity<List<Withdrawal>>getWithdrawalHsitory(
            @RequestHeader("Authorization")String jwt
) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);
    List<Withdrawal>withdrawals=withdrawalService.getCustomerWithdrawalHistory(customer);
    return ResponseEntity.ok().body(withdrawals);
}

@GetMapping("/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>>getAllWithdrawalRequest(
           @RequestHeader ("Authorization")String jwt
) throws Exception {

    Customer customer = customerServices.findCustomerByJwt(jwt);
    List<Withdrawal>withdrawals=withdrawalService.getAllWithdrawalRequest();
    return ResponseEntity.ok().body(withdrawals);
}
}
