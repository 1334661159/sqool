package com.abuqool.sqool.repo.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abuqool.sqool.dao.common.ListingInfo;

public interface ListingRepo extends JpaRepository<ListingInfo, Integer>{

    ListingInfo findBySchoolCodeAndProductId(String schoolCode, int productId);

    List<ListingInfo> findBySchoolCode(String schoolCode);


}
