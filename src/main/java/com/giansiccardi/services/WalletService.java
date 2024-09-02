package com.giansiccardi.services;

import com.giansiccardi.dto.WalletTransaction;
import com.giansiccardi.enums.OrderType;
import com.giansiccardi.enums.WalletTransactionType;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.Order;
import com.giansiccardi.models.Wallet;
import com.giansiccardi.repository.WalletRepository;
import com.giansiccardi.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {


    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;


    public Long generateRandomCVU() {
        long min = 1000000000L;
        long max = 9999999999L;

        long cvu = min + (long)(Math.random() * ((max - min) + 1));
        return cvu;
    }
public Wallet getUserWallet(Customer customer){
    Wallet wallet =walletRepository.findByCustomerId(customer.getId());
    if(wallet==null){
        wallet = new Wallet();
        wallet.setId(generateRandomCVU());
        wallet.setCustomer(customer);
        walletRepository.save(wallet);

    }
    return wallet;

}
public Wallet addBalance(Wallet wallet,Long money){
    BigDecimal balance=wallet.getBalance();
    BigDecimal newBalance=balance.add(BigDecimal.valueOf(money));
    wallet.setBalance(newBalance);
    return walletRepository.save(wallet);
}

public Wallet findById(Long id) throws Exception {
    Optional<Wallet>wallet=walletRepository.findById(id);

    if(wallet.isPresent()){
        return wallet.get();
    }

    throw new Exception("wallet no encontrada");
}

public Wallet walletToWalletTransfe(Customer sender ,Wallet receiveWallet,Long amount) throws Exception {
Wallet senderWallet=getUserWallet(sender);
if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0){
    throw new Exception("Saldo insuficiente");
}

BigDecimal senderBalance=senderWallet
        .getBalance()
        .subtract(BigDecimal.valueOf(amount));
senderWallet.setBalance(senderBalance);
walletRepository.save(senderWallet);
BigDecimal receiverBalancer=receiveWallet.getBalance().add(BigDecimal.valueOf(amount));
receiveWallet.setBalance(receiverBalancer);
walletRepository.save(receiveWallet);

    WalletTransaction senderTransaction = new WalletTransaction();
    senderTransaction.setWallet(senderWallet);
    senderTransaction.setWalletTransactionType(WalletTransactionType.WAllET_TRANSFER);
    senderTransaction.setDate(LocalDate.now());
    senderTransaction.setPurpose("Transferencia realizada");
    senderTransaction.setAmount(amount);
    walletTransactionRepository.save(senderTransaction);
return senderWallet;
}

public Wallet payOrderPayment(Order order , Customer customer) throws Exception {
Wallet wallet=getUserWallet(customer);
if(order.getOrderType().equals(OrderType.BUY)){
    BigDecimal newBalance=wallet.getBalance().subtract(order.getPrice());
    if(newBalance.compareTo(order.getPrice())<0){
        throw new Exception("Saldo insufciente para la transaccion");
    }
    wallet.setBalance(newBalance);
}
else {
    BigDecimal newBalance=wallet.getBalance().add(order.getPrice());
    wallet.setBalance(newBalance);
}
walletRepository.save(wallet);
return wallet;
}
}
