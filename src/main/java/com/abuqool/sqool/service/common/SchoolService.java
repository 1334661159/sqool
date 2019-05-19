package com.abuqool.sqool.service.common;

import com.abuqool.sqool.dao.common.SchoolInfo;
import com.abuqool.sqool.vo.School;

public interface SchoolService {


    int SCHOOL_STATUS_NORMAL = 0;
    int SCHOOL_STATUS_DELETED = -1;

    String PHASE_RETAIL = "";
    String PHASE_PRESALE = "PRESALE";
    String PHASE_SHIPPING = "SHIPPING";
    

    School findSchool(String schoolCode);

    SchoolInfo findSchoolInfoByCode(String schoolCode);

}
