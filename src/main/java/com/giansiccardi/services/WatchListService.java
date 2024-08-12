package com.giansiccardi.services;

import com.giansiccardi.models.Coin;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.WatchList;
import com.giansiccardi.repository.WatchListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchListService {

private final WatchListRepository watchListRepository;
    public WatchList findByCustomerWatchList(Long customerId) throws Exception {
WatchList watchList=watchListRepository.findByCustomerId(customerId);
if(watchList==null){
    throw new Exception("watchList not found");
}
return watchList;
    }

    public WatchList createWatchList(Customer customer){
WatchList watchList = new WatchList();
watchList.setCustomer(customer);
return watchListRepository.save(watchList);
    }

    public WatchList findById(Long id) throws Exception {
        Optional<WatchList>watchListOptional=watchListRepository.findById(id);
        if(watchListOptional.isEmpty()){
            throw new Exception("WatchList no encontrada");
        }
        return watchListOptional.get();
    }
    public Coin addItemToWatchlist(Coin coin ,Customer customer) throws Exception {
WatchList watchList= findByCustomerWatchList(customer.getId());
if(watchList.getCoins().contains(coin)){
    watchList.getCoins().remove(coin);
}
else watchList.getCoins().add(coin);
 watchListRepository.save(watchList);

 return coin;
    }
}
