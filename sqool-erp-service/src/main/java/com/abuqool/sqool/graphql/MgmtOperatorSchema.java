package com.abuqool.sqool.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abuqool.sqool.auth.AuthService;
import com.abuqool.sqool.auth.MgmtGraphQLOps;
import com.abuqool.sqool.service.mgmt.OperatorService;
import com.abuqool.sqool.vo.Operator;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@GraphQLApi
@Service
public class MgmtOperatorSchema {

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private AuthService authService;

    @GraphQLMutation
    public Operator admLogin(String code) {
        Operator o = authService.wechatLogon(code);
        return o;
    }

    @GraphQLMutation
    public Operator admSaveOperator(String token, String name, String company, String phone, String wechat) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_SAVE_OPERATOR)) {
            return null;
        }
        Operator o = operatorService.saveOperator(token, name, company, phone, wechat);
        return o;
    }

    @GraphQLQuery
    public Operator admWhoAmI(String token) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.QUERY_WHOAMI)) {
            return null;
        }
        Operator u = operatorService.findOperatorByToken(token);
        return u;
    }
}
