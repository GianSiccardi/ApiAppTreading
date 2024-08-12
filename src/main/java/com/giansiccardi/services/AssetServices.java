package com.giansiccardi.services;

import com.giansiccardi.models.Asset;
import com.giansiccardi.models.Coin;
import com.giansiccardi.models.Customer;
import com.giansiccardi.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServices {


    private final AssetRepository assetRepository;

public Asset crearAsset(Customer customer, Coin coin, double quantity){
Asset asset = new Asset();
asset.setCustomer(customer);
asset.setCoin(coin);
asset.setQuantity(quantity);
asset.setBuyPrice(coin.getCurrentPrice());
return assetRepository.save(asset);
}

public Asset getAssetById(Long assetId) throws Exception {

    return assetRepository.findById(assetId).orElseThrow(()->new Exception("Asset no encontrado"));

}
    public List<Asset>  getUsersAsset(Long customerId){
        return assetRepository.findByCustomerId(customerId);
    }
    public Asset getAssetByCustomerIdAndId(Long customerId ,Long assetId){
return null;
}



public Asset updateAsset(Long assetId,double quantity) throws Exception {
Asset oldAsset=getAssetById(assetId);
oldAsset.setQuantity(quantity);
return assetRepository.save(oldAsset);

}


public Asset findAssetByCustomerIdAndConId(Long customerId, String coindId){
return assetRepository.findByCustomerIdAndCoinId(customerId,coindId);
}

public void deleteAsset(Long assetId){
assetRepository.deleteById(assetId);
}


}
