package com.giansiccardi.services;

import com.giansiccardi.enums.WithdrawalStatus;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.Withdrawal;
import com.giansiccardi.repository.WithdrawalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WithdrawalService {


    private final WithdrawalRepository withdrawalRepository;
public Withdrawal requestWithdrawal(Long amount, Customer customer){
Withdrawal withdrawal = new Withdrawal();
withdrawal.setAmount(amount);
withdrawal.setCustomer(customer);
withdrawal.setWithdrawalStatus(WithdrawalStatus.PENDING);
return withdrawalRepository.save(withdrawal);
}

public Withdrawal procedWithdrawal(Long withdrawalId,boolean accept) throws Exception {
    Optional<Withdrawal>withdrawal=withdrawalRepository.findById(withdrawalId);
    if(withdrawal.isEmpty()){
        throw new Exception("no encontrado");
    }

    Withdrawal withdrawal1=withdrawal.get();
    withdrawal1.setDateTime(LocalDateTime.now());
    if(accept) {
        withdrawal1.setWithdrawalStatus(WithdrawalStatus.SUCCESS);
    }else {
        withdrawal1.setWithdrawalStatus(WithdrawalStatus.PENDING);
    }
    return withdrawalRepository.save(withdrawal1);
}
public List<Withdrawal> getCustomerWithdrawalHistory(Customer customer){
return withdrawalRepository.findByCustomerId(customer.getId());
}
    public List<Withdrawal> getAllWithdrawalRequest(){
return withdrawalRepository.findAll();
    }
}
