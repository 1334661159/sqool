package com.abuqool.sqool.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abuqool.sqool.auth.AuthService;
import com.abuqool.sqool.auth.MgmtGraphQLOps;
import com.abuqool.sqool.service.mgmt.MgmtSchoolService;
import com.abuqool.sqool.vo.Event;
import com.abuqool.sqool.vo.School;
import com.abuqool.sqool.vo.SchoolClz;

import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@GraphQLApi
@Service
public class MgmtSchoolSchema {

    @Autowired
    private AuthService authService;

    @Autowired
    private MgmtSchoolService schoolService;

    @GraphQLMutation
    public Event admSaveEvent(
            String token,
            String schoolCode,
            String title,
            String preSaleStart,
            String preSaleEnd,
            String shippingStart,
            String shippingEnd,
            String paymentMode,
            String deliveryMode) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_SAVE_EVENT)) {
            return null;
        }
        Event e = schoolService.saveEvent(
                schoolCode, title, preSaleStart, preSaleEnd, shippingStart, shippingEnd, paymentMode, deliveryMode);
        return e;
    }

    @GraphQLQuery
    public List<School> admSchools(String token) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.QUERY_ALL_SCHOOLS)) {
            return null;
        }

        List<School> list = schoolService.getSchools();
        return list;
    }
    
    @GraphQLMutation
    public School admSaveSchool(
            String token,
            int id,
            String code,
            String title,
            String dispName,
            String logoUrl,
            String bgUrl){
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_SAVE_SCHOOL)) {
            return null;
        }

        School s = schoolService.saveSchool(id, code, title, dispName, logoUrl, bgUrl);
        return s;
    }

    @GraphQLMutation
    public String admDisableSchool(String token, String schoolCode) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_DISABLE_SCHOOL)) {
            return null;
        }
        return schoolService.disableSchool(schoolCode);
    }

    @GraphQLQuery
    public School admSchoolDetails(
            String token,
            String schoolCode) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.QUERY_SCHOOL)) {
            return null;
        }

        School s = schoolService.findSchool(schoolCode);
        return s;
    }


    @GraphQLQuery
    public Event event(@GraphQLContext School school) {
        Event e = schoolService.findEvent4School(school.getCode());
        return e;
    }

    @GraphQLMutation
    public String admGenQr4School(String token, String schoolCode) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_GEN_SCHOOL_QR)) {
            return null;
        }
        schoolService.genQr4School(schoolCode);
        return schoolCode;
    }

    @GraphQLMutation
    public School admSaveClz4School(String token, String schoolCode, List<SchoolClz> clz) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_SAVE_SCHOOL_CLZ)) {
            return null;
        }
        School s = schoolService.saveClz(schoolCode, clz);
        return s;
    }
}
