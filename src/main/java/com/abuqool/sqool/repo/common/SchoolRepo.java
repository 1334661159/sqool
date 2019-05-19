package com.abuqool.sqool.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abuqool.sqool.dao.common.SchoolInfo;

public interface SchoolRepo extends JpaRepository<SchoolInfo, Integer>{

    SchoolInfo findByCode(String schoolCode);

}
