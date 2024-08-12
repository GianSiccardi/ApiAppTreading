package com.giansiccardi.controllers;

import com.giansiccardi.models.Asset;
import com.giansiccardi.models.Customer;
import com.giansiccardi.services.AssetServices;
import com.giansiccardi.services.CustomerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetServices assetServices;

    private final CustomerServices customerServices;


    @GetMapping("/{assetId}")
    public ResponseEntity<Asset>getAssetById(@PathVariable Long assetId) throws Exception {
        Asset asset=assetServices.getAssetById(assetId);
        return ResponseEntity.ok().body(asset);

    }

    @GetMapping("/coin/{coindId}/customer")
   public ResponseEntity<Asset>getAssetByCustomerIdAndCoinId(
           @PathVariable String coindId,
           @RequestHeader("Authorization")String jwt
    ) throws Exception {
        Customer customer=customerServices.findCustomerByJwt(jwt);
        Asset asset=assetServices.findAssetByCustomerIdAndConId(customer.getId(),coindId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping
    public ResponseEntity<List<Asset>>getAssetsForCustomer(
            @RequestHeader("Authorization")String jwt
    ) throws Exception {
        Customer customer=customerServices.findCustomerByJwt(jwt);
        List<Asset>assets=assetServices.getUsersAsset(customer.getId());
       return ResponseEntity.ok().body(assets);
    }


}
