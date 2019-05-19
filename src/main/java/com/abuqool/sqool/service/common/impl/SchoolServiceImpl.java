package com.abuqool.sqool.service.common.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.abuqool.sqool.dao.common.SchoolInfo;
import com.abuqool.sqool.repo.common.SchoolRepo;
import com.abuqool.sqool.service.common.SchoolService;
import com.abuqool.sqool.vo.School;

public class SchoolServiceImpl implements SchoolService{

    protected static final Logger logger = LoggerFactory.getLogger(SchoolServiceImpl.class);


    @Autowired
    private SchoolRepo schoolRepo;

    @Override
    public School findSchool(String schoolCode) {
        SchoolInfo s = findSchoolInfoByCode(schoolCode);
        return School.populate(s);
    }

    @Override
    public SchoolInfo findSchoolInfoByCode(String schoolCode) {
        SchoolInfo s = schoolRepo.findByCode(schoolCode);
//        if(s == null) {
//            logger.info("Cannot find school by code {"+schoolCode+"}");
//            throw new BizException(BizException.Code.ADMIN_INVALID_PARAM);
//        }
//        if(s.getStatus() == SCHOOL_STATUS_DELETED) {
//            throw new BizException(BizException.Code.ADMIN_OBJECT_DELETED);
//        }
        return s;
    }

}
