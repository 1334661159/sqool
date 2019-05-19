package com.abuqool.sqool.service.mgmt;

import java.util.List;

import com.abuqool.sqool.service.common.SchoolService;
import com.abuqool.sqool.vo.Event;
import com.abuqool.sqool.vo.School;
import com.abuqool.sqool.vo.SchoolClz;

public interface MgmtSchoolService extends SchoolService{

    List<School> getSchools();

    School saveSchool(int id, String schoolCode, String title, String dispName, String logoUrl, String bgUrl);

    String disableSchool(String schoolCode);

    Event findEvent4School(String schoolCode);

    Event saveEvent(String schoolCode, String title,
            String preSaleStart, String preSaleEnd,
            String shippingStart, String shippingEnd, String paymentMode, String deliveryMode);

    void genQr4School(String schoolCode);

    School saveClz(String schoolCode, List<SchoolClz> clz);
}
