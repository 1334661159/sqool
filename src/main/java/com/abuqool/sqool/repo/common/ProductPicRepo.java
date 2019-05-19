package com.abuqool.sqool.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abuqool.sqool.dao.common.ProductPicInfo;

public interface ProductPicRepo extends JpaRepository<ProductPicInfo, Integer>{
    
}
