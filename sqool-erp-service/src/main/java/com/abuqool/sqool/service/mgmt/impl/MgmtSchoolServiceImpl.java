package com.abuqool.sqool.service.mgmt.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.abuqool.sqool.biz.BizException;
import com.abuqool.sqool.dao.common.EventInfo;
import com.abuqool.sqool.dao.common.EventPhaseInfo;
import com.abuqool.sqool.dao.common.SchoolClassInfo;
import com.abuqool.sqool.dao.common.SchoolInfo;
import com.abuqool.sqool.integration.wx.WxService;
import com.abuqool.sqool.repo.common.EventPhaseRepo;
import com.abuqool.sqool.repo.common.EventRepo;
import com.abuqool.sqool.repo.common.SchoolClassRepo;
import com.abuqool.sqool.repo.common.SchoolRepo;
import com.abuqool.sqool.service.common.impl.SchoolServiceImpl;
import com.abuqool.sqool.service.file.StorageService;
import com.abuqool.sqool.service.mgmt.MgmtSchoolService;
import com.abuqool.sqool.vo.Event;
import com.abuqool.sqool.vo.School;
import com.abuqool.sqool.vo.SchoolClz;

@Service
public class MgmtSchoolServiceImpl extends SchoolServiceImpl implements MgmtSchoolService{

    protected static final Logger logger = LoggerFactory.getLogger(MgmtSchoolServiceImpl.class);

    @Autowired
    private SchoolRepo schoolRepo;

    @Autowired
    private WxService wxService;

    @Autowired
    private StorageService storageService;

    @Override
    public void genQr4School(String schoolCode) {
        /*
         * file = wxService.genQr()
         * storageSerice.store(file)
         */
        byte[] data = wxService.getQr4Scene("school_code="+schoolCode);
        if(data != null) {
            storageService.store("qr", schoolCode+"_qr.png", data);
        }
        logger.info("https://cdn-1258157285.cos.ap-shanghai.myqcloud.com/abuqool/qr/"+schoolCode+"_qr.png");
    }

    @Override
    public School saveClz(String schoolCode, List<SchoolClz> clz) {
        SchoolInfo s = findSchoolInfoByCode(schoolCode);
        Date now = new Date();
        Set<SchoolClassInfo> set = new HashSet<>();
        Set<String> grades = new HashSet<>();
        Map<Integer, SchoolClassInfo> map = new HashMap<>();
        if(s.getClzSet() != null && !s.getClzSet().isEmpty()) {
            for(SchoolClassInfo c : s.getClzSet()) {
                map.put(c.getId(), c);
            }
        }
        for(SchoolClz sc : clz) {
            SchoolClassInfo c;
            if(sc.getId() > 0) {
                c = map.remove(sc.getId());
            }else {
                c = new SchoolClassInfo();
                c.setCreateTime(now);
            }
            c.setUpdateTime(now);
            c.setTitle(sc.getTitle());
            c.setGrade(sc.getGrade());
            c.setSchool(s);
            schoolClzRepo.save(c);
            set.add(c);
            grades.add(sc.getGrade());
        }
        s.setGrades(School.gradesToString(grades));
        s.setClzSet(set);
        schoolRepo.save(s);
        for (Entry<Integer, SchoolClassInfo> entry : map.entrySet()) {
            schoolClzRepo.delete(entry.getValue());
        }
        return School.populate(s);
    }

    @Override
    public String disableSchool(String schoolCode) {
        SchoolInfo s = findSchoolInfoByCode(schoolCode);
        s.setStatus(SCHOOL_STATUS_DELETED);
        schoolRepo.save(s);
        return schoolCode;
    }

    @Override
    public Event findEvent4School(String schoolCode) {
        List<EventInfo> events = eventRepo.findOpenEventsForSchool(schoolCode, new Date());
        if(events != null && events.size() > 0) {
            EventInfo event = events.get(0);//assume only 1 open event is allowed
            if(event.getPhases() != null && event.getPhases().size() == 2) {
                List<EventPhaseInfo> phases = new ArrayList<>(event.getPhases());
                phases.sort(new Comparator<EventPhaseInfo>() {
                    @Override
                    public int compare(EventPhaseInfo o1, EventPhaseInfo o2) {
                        return o1.getId() - o2.getId();
                    }
                });
                return Event.populate(event, phases.get(0), phases.get(1));
            }
        }
        logger.info("No open event found for school {"+schoolCode+"}");
        return null;
    }

    @Override
    public Event saveEvent(
            String schoolCode, String title,
            String preSaleStart, String preSaleEnd,
            String shippingStart, String shippingEnd, String paymentMode, String deliveryMode) {

        Date now = new Date();
        Date preSaleStartDate = Event.toDate(preSaleStart);
        Date preSaleEndDate = Event.toDate(preSaleEnd);
        Date shippingStartDate = Event.toDate(shippingStart);
        Date shippingEndDate = Event.toDate(shippingEnd);
        if(shippingEndDate.before(now)) {
            logger.info("End date of event should be sometime in the future");
            throw new BizException(BizException.Code.ADMIN_INCORRECT_PARAM);
        }
        if(preSaleEndDate.before(preSaleStartDate)
           || shippingStartDate.before(preSaleEndDate)
           || shippingEndDate.before(shippingStartDate)) {
            logger.info("Given phase start/end date are not in correct sequence");
            throw new BizException(BizException.Code.ADMIN_INCORRECT_PARAM);
        }

        EventInfo e;
        List<EventInfo> events = eventRepo.findOpenEventsForSchool(schoolCode, now);
        if(events != null && events.size() > 0) {
            e = events.get(0);
        }else {
            e = new EventInfo();
            e.setCreateTime(now);
        }
        e.setSchoolCode(schoolCode);
        e.setTitle(title);
        e.setStartTime(Event.toDate(preSaleStart));
        e.setEndTime(Event.toDate(shippingEnd));
        e.setPaymentMode(School.guardPaymentMode(paymentMode));
        e.setDeliveryMode(School.guardDeliveryMode(deliveryMode));
        e.setUpdateTime(now);
        eventRepo.save(e);

        EventPhaseInfo preSale;
        EventPhaseInfo shipping;
        if(e.getPhases() != null && e.getPhases().size() == 2) {
            List<EventPhaseInfo> phases = new ArrayList<>(e.getPhases());
            phases.sort(new Comparator<EventPhaseInfo>() {
                @Override
                public int compare(EventPhaseInfo o1, EventPhaseInfo o2) {
                    return o1.getId() - o2.getId();
                }
            });
            preSale = phases.get(0);
            shipping = phases.get(1);
        }else {
            preSale = new EventPhaseInfo();
            preSale.setTitle(PHASE_PRESALE);
            preSale.setSchoolCode(schoolCode);
            preSale.setCreateTime(now);
            shipping = new EventPhaseInfo();
            shipping.setTitle(PHASE_SHIPPING);
            shipping.setSchoolCode(schoolCode);
            shipping.setCreateTime(now);
        }
        preSale.setStartTime(Event.toDate(preSaleStart));
        preSale.setEndTime(Event.toDate(preSaleEnd));
        preSale.setEvent(e);
        preSale.setUpdateTime(now);
        eventPhaseRepo.save(preSale);//presale's id is always smaller than shipping

        shipping.setStartTime(Event.toDate(shippingStart));
        shipping.setEndTime(Event.toDate(shippingEnd));
        shipping.setEvent(e);
        shipping.setUpdateTime(now);
        eventPhaseRepo.save(shipping);
        SchoolInfo school = findSchoolInfoByCode(schoolCode);
        refreshSchoolPhase(now, school);
        return Event.populate(e, preSale, shipping);
    }


    @Autowired
    private SchoolClassRepo schoolClzRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private EventPhaseRepo eventPhaseRepo;

//    @Scheduled(cron = "0 * * * * *")
//    public void schedulerHeartbeat() {
//        logger.info(" **** SchoolServiceImpl.schdulerHeartbeat()");//for testing
//    }

//    @Scheduled(cron = "0 * * * * *")//for testing, per minute
    @Scheduled(cron="0 1 0 * * *")//everyday, 00:01:00, given all phase starts at 00:00:00, this should be safe enough
    @Transactional
    public void updateSchoolsForPhase() {
        logger.info("[admin_scheduled] update school for phase info");
        List<SchoolInfo> schools = schoolRepo.findAll();
        if(schools != null) {
            Date now = new Date();
            for(SchoolInfo school : schools) {
                try {
                    refreshSchoolPhase(now, school);
                }catch(Exception ex) {
                    logger.error("Error on refreshing phase for school {"+school.getCode()+"}", ex);
                }
            }
        }
    }

    private void refreshSchoolPhase(Date now, SchoolInfo school) {
        if(school.getStatus() != SCHOOL_STATUS_DELETED) {
            EventPhaseInfo phase = eventPhaseRepo.findCurrentPhaseForSchool(school.getCode(), now);
            String phaseStr = PHASE_RETAIL;
            if(phase != null) {
                phaseStr = phase.getTitle();
            }
            Event event = findEvent4School(school.getCode());
            String paymentMode = "";
            String deliveryMode = "";
            if(event != null) {
                paymentMode = School.guardPaymentMode(event.getPaymentMode());
                deliveryMode = School.guardDeliveryMode(event.getDeliveryMode());
            }
            if(!phaseStr.equals(school.getPhase())
                    || !paymentMode.equals(school.getPaymentMode())
                    || !deliveryMode.equals(school.getDeliveryMode())) {
                school.setPhase(phaseStr);
                school.setPaymentMode(School.guardPaymentMode(paymentMode));
                school.setDeliveryMode(School.guardDeliveryMode(deliveryMode));
                school.setUpdateTime(now);
                schoolRepo.save(school);
                logger.info("School {"+school.getCode()+"} is now in phase {"+phaseStr+"}");
            }
        }
    }

    @Override
    public List<School> getSchools() {
        List<SchoolInfo> schools = schoolRepo.findAll();
        if(schools != null) {
            List<School> list = new ArrayList<>(schools.size());
            for(SchoolInfo school : schools) {
                if(school.getStatus() != SCHOOL_STATUS_DELETED) {
                    int numOfListings = school.getProducts().size();//TODO optimize?
//                    int numOfListings = productRepo.getNumOfListingsForSchool(school.getCode());
                    list.add(School.populate(school, numOfListings));
                }
            }
            return list;
        }
        return null;
    }

    @Override
    @Transactional
    public School saveSchool(//TODO remove id
            int id, String schoolCode,
            String title, String dispName, String logoUrl, String bgUrl) {
        if(StringUtils.isEmpty(schoolCode)) {
            logger.info("Empty schoolCode is not allowed");
            throw new BizException(BizException.Code.ADMIN_MISSING_REQUIRED_PARAM);
        }
        if(StringUtils.isEmpty(title)) {
            logger.info("Empty title is not allowed");
            throw new BizException(BizException.Code.ADMIN_MISSING_REQUIRED_PARAM);
        }
        if(StringUtils.isEmpty(dispName)) {
            logger.info("Empty dispName is not allowed");
            throw new BizException(BizException.Code.ADMIN_MISSING_REQUIRED_PARAM);
        }

        logger.info("Saving school code = {"+schoolCode+"}");
        Date now = new Date();
        SchoolInfo s = schoolRepo.findByCode(schoolCode);//DO NOT use the method with validation
        if(s == null) {
            s = new SchoolInfo();
            s.setCreateTime(now);
            s.setCode(schoolCode);//schoolCode is one time stuff
            s.setStatus(SCHOOL_STATUS_NORMAL);
            genQr4School(schoolCode);
        }

        s.setTitle(title);
        s.setDispName(dispName);
        s.setLogoUrl(logoUrl);
        s.setBgUrl(bgUrl);
        s.setUpdateTime(now);
        schoolRepo.save(s);
        return School.populate(s);
    }
}
