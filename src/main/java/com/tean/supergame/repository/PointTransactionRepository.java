package com.tean.supergame.repository;

import com.tean.supergame.model.entity.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction,Integer> {

    List<PointTransaction> findAllByUserId(String userId);
}