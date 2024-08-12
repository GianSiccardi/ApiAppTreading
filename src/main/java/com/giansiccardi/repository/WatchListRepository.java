package com.giansiccardi.repository;

import com.giansiccardi.models.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList,Long> {

WatchList findByCustomerId(Long customerId);
}
