package com.giansiccardi.services;

import com.giansiccardi.enums.OrderType;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.Order;
import com.giansiccardi.models.Wallet;
import com.giansiccardi.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {


    private final WalletRepository walletRepository;
public Wallet getUserWallet(Customer customer){
    Wallet wallet =walletRepository.findByCustomerId(customer.getId());
    if(wallet==null){
        wallet = new Wallet();
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
