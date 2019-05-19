package com.abuqool.sqool.service.mgmt;

import com.abuqool.sqool.dao.mgmt.OperatorInfo;
import com.abuqool.sqool.vo.Operator;

public interface OperatorService {

    Operator findOperatorByToken(String token);

    OperatorInfo findOperatorInfoByOpenId(String openId);

    void saveOperatorInfo(OperatorInfo o);

    OperatorInfo findOperatorInfoByToken(String token);

    Operator saveOperator(String token, String name, String company, String phone, String wechat);

}
