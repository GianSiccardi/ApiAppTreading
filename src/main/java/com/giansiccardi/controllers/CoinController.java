package com.giansiccardi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giansiccardi.models.Coin;
import com.giansiccardi.services.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
@RequiredArgsConstructor
public class CoinController {

private final CoinService coinService;

private final ObjectMapper objectMapper;


@GetMapping
public ResponseEntity<List<Coin>>getCoinList(@RequestParam(required = false,name = "page") int page){
    List<Coin>coins=coinService.getCoinList(page);
    return ResponseEntity.status(HttpStatus.OK).body(coins);
}

    @GetMapping("/{id}/chart")
    public ResponseEntity<?>getMarketChart(
            @PathVariable String id,
            @RequestParam("days")int days) throws JsonProcessingException {
        String response =coinService.getMarketChart(id,days);
        JsonNode jsonNode=objectMapper.readTree(response);
        return ResponseEntity.status(HttpStatus.OK).body(jsonNode);
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode>searchi(@RequestParam ("q")String keyword) throws JsonProcessingException {
        String coin=coinService.getCoinDetails(keyword);
        JsonNode jsonNode=objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);

    }


    @GetMapping("/top50")
    public ResponseEntity<JsonNode>getTop50CoinByMarketCaprRank() throws JsonProcessingException {
    String coin=coinService.getTop50CoinsByMarketCapRank();
        JsonNode jsonNode=objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);
    }
    @GetMapping("/treading")
    public ResponseEntity<JsonNode>traeding() throws JsonProcessingException {
        String coin=coinService.getTreadingCoins();
        JsonNode jsonNode=objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);

    }


    @GetMapping("/details/{id}")
    public ResponseEntity<JsonNode>CoinsDetails(@PathVariable String id) throws JsonProcessingException {
        String coin=coinService.getCoinDetails(id);
        JsonNode jsonNode=objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);

}
}
