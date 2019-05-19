package com.abuqool.sqool.repo.common;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abuqool.sqool.dao.common.ProductInfo;

public interface ProductRepo extends JpaRepository<ProductInfo, Integer>{
//    List<ProductInfo> findBySchools_Code(String schoolCode, Pageable pageable);
//
//    List<ProductInfo> findBySchools_Code(String schoolCode);
//
//    List<ProductInfo> findByCategoryAndSchools_Code(String category, String schoolCode);
//
//    List<ProductInfo> findByGenderAndSchools_Code(String gender, String schoolCode);

    List<ProductInfo> findByCodeIn(Set<String> codes);

    ProductInfo findByCode(String code);

//    @Query("SELECT COUNT(1) FROM ProductInfo p WHERE p.schoolCode = :code")
//    int getNumOfListingsForSchool(@Param("code") String schoolCode);
}
