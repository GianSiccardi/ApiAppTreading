package com.giansiccardi.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giansiccardi.models.Coin;
import com.giansiccardi.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoinService {
   private final ObjectMapper objectMapper;

    private final CoinRepository coinRepository;
public List<Coin>getCoinList(int page){

    String url="https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page="+page;
    RestTemplate restTemplate = new RestTemplate();
    try{
        HttpHeaders headers= new HttpHeaders();
        HttpEntity<String>entity= new HttpEntity<String>("parametes",headers);
        ResponseEntity<String>response=restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
        List<Coin> coinList =objectMapper.readValue(response.getBody(),
                new TypeReference<List<Coin>>(){});
        return coinList;
    } catch (JsonMappingException e) {
        throw new RuntimeException(e);
    } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
    }



}

    public String getMarketChart(String coinId,int days){
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers= new HttpHeaders();
            HttpEntity<String>entity= new HttpEntity<String>("parametes",headers);
            ResponseEntity<String>response=restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
         return response.getBody();

        }catch (HttpClientErrorException E){
            throw new RuntimeException(E.getMessage());
        }
}

    public String getCoinDetails(String coinId){
        String url="https://api.coingecko.com/api/v3/coins/"+coinId;
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers= new HttpHeaders();
            HttpEntity<String>entity= new HttpEntity<String>("parametes",headers);
            ResponseEntity<String>response=restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
            JsonNode jsonNode=objectMapper.readTree(response.getBody());


            Coin coin = new Coin();
            coin.setId(jsonNode.get("id").asText());
            coin.setName(jsonNode.get("name").asText());
            coin.setSymbol(jsonNode.get("symbol").asText());
            coin.setImage(jsonNode.get("image").get("large").asText());

           JsonNode marketData=jsonNode.get("market_data");

            coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
            coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
            coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
            coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
            coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
            coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
            coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
            coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
            coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asLong());
            coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
            coin.setTotalSupply(marketData.get("total_supply").asDouble());
            coinRepository.save(coin);

            return response.getBody();

        }catch (HttpClientErrorException E){
            throw new RuntimeException(E.getMessage());
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Coin findById(String coinId) throws Exception {
        Optional<Coin>optionalCoins=coinRepository.findById(coinId);
        if(optionalCoins.isEmpty()){
            throw new Exception("Moneda no encontrada");
        }
        return optionalCoins.get();
}

    public String searchiCoin(String keyword){
        String url="https://api.coingecko.com/api/v3/search?query="+keyword;
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers= new HttpHeaders();
            HttpEntity<String>entity= new HttpEntity<String>("parametes",headers);
            ResponseEntity<String>response=restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
            return response.getBody();

        }catch (HttpClientErrorException E){
            throw new RuntimeException(E.getMessage());
        }
    }

    public String getTop50CoinsByMarketCapRank(){
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers= new HttpHeaders();
            HttpEntity<String>entity= new HttpEntity<String>("parametes",headers);
            ResponseEntity<String>response=restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
            return response.getBody();

        }catch (HttpClientErrorException E){
            throw new RuntimeException(E.getMessage());
        }
    }

    public String getTreadingCoins(){
        String url="https://api.coingecko.com/api/v3/search/trending";
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers= new HttpHeaders();
            HttpEntity<String>entity= new HttpEntity<String>("parametes",headers);
            ResponseEntity<String>response=restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
            return response.getBody();

        }catch (HttpClientErrorException E){
            throw new RuntimeException(E.getMessage());
        }
    }
}
