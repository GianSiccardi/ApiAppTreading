package com.giansiccardi.controllers;

import com.giansiccardi.models.Coin;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.WatchList;
import com.giansiccardi.services.CoinService;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

private final WatchListService watchListService;
private final CustomerServices customerServices;
private final CoinService coinService;

@GetMapping("/customer")
    public ResponseEntity<WatchList>getCustomerWatchliat(
            @RequestHeader ("Authorization") String jwt
) throws Exception {

    Customer customer=customerServices.findCustomerByJwt(jwt);
    WatchList watchList=watchListService.findByCustomerWatchList(customer.getId());
    return ResponseEntity.ok(watchList);
}

@PostMapping("/create")
    public ResponseEntity<WatchList>createWatchlist(
            @RequestHeader("Authorization") String jwt
) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);
    WatchList createdWachtlist=watchListService.createWatchList(customer);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdWachtlist);
}
@GetMapping("/{watchlistId}")
    public ResponseEntity<WatchList>getWatchlistById(
            @PathVariable Long watchlistId )throws Exception {
                WatchList watchList=watchListService.findById(watchlistId);
    return ResponseEntity.ok(watchList);
}
@PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin>addItemToWatchlist(
            @RequestHeader("Authorization")String jwt,
            @PathVariable String coinId
) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);
    Coin coin=coinService.findById(coinId);
    Coin addeCoin=watchListService.addItemToWatchlist(coin,customer);
return ResponseEntity.ok(addeCoin);
}


}
