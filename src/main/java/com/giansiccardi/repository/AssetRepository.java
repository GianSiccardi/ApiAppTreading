package com.giansiccardi.repository;

import com.giansiccardi.models.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository  extends JpaRepository<Asset,Long> {

List<Asset> findByCustomerId(Long customerId);


Asset findByCustomerIdAndCoinId(Long customerId, String coinId);

}
