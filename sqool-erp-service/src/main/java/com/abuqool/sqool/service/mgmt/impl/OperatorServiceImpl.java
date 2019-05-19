package com.abuqool.sqool.service.mgmt.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abuqool.sqool.dao.mgmt.OperatorInfo;
import com.abuqool.sqool.repo.mgmt.OperatorRepo;
import com.abuqool.sqool.service.mgmt.OperatorService;
import com.abuqool.sqool.vo.Operator;

@Service
public class OperatorServiceImpl implements OperatorService {

    protected static final Logger logger = LoggerFactory.getLogger(OperatorServiceImpl.class);

    @Autowired
    private OperatorRepo operatorRepo;

    @Override
    public Operator findOperatorByToken(String token) {
        OperatorInfo operator = operatorRepo.findBySessionToken(token);
        if (operator == null) {
            return null;
        }
        return Operator.populate(operator);
    }

    @Override
    public OperatorInfo findOperatorInfoByOpenId(String openId) {
        return operatorRepo.findByOpenId(openId);
    }

    @Override
    public void saveOperatorInfo(OperatorInfo o) {
        operatorRepo.save(o);
    }

    @Override
    public OperatorInfo findOperatorInfoByToken(String token) {
        return operatorRepo.findBySessionToken(token);
    }

    @Override
    public Operator saveOperator(String token, String name, String company, String phone, String wechat) {
        OperatorInfo o = operatorRepo.findBySessionToken(token);
        if(o != null) {
            o.setName(name);
            o.setCompany(company);
            o.setPhone(phone);
            o.setWechat(wechat);
            operatorRepo.save(o);
            return Operator.populate(o);
        }
        return null;
    }

}
