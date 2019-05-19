package com.abuqool.sqool.repo.common;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abuqool.sqool.dao.common.ProductOrderUnitInfo;

public interface ProductOrderUnitRepo extends JpaRepository<ProductOrderUnitInfo, Integer>{

    Set<ProductOrderUnitInfo> findByOrderId(int orderId);

    
}
