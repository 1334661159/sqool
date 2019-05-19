package com.abuqool.sqool.repo.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abuqool.sqool.dao.common.ProductStockUnitInfo;

public interface ProductStockUnitRepo extends JpaRepository<ProductStockUnitInfo, Integer>{

    ProductStockUnitInfo findByCode(String code);

    @Query("SELECT u FROM ProductStockUnitInfo u WHERE u.code LIKE :code%")
    List<ProductStockUnitInfo> findAllByCodeLike(@Param("code") String code);

}
