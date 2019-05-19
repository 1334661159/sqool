package com.abuqool.sqool.repo.mgmt;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abuqool.sqool.dao.mgmt.OperatorInfo;

public interface OperatorRepo extends JpaRepository<OperatorInfo, Integer> {
    public OperatorInfo findByOpenId(String openId);

    public OperatorInfo findBySessionToken(String token);
}
