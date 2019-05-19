package com.abuqool.sqool.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.abuqool.sqool.dao.common.SchoolClassInfo;
import com.abuqool.sqool.dao.common.SchoolInfo;
import com.abuqool.sqool.service.common.impl.SchoolServiceImpl;

public class School {
    protected static final Logger logger = LoggerFactory.getLogger(SchoolServiceImpl.class);

    private int id;
    private String code;
    private String title;
    private String logoUrl;
    private String bgUrl;
    private String dispName;
    private int numOfListings;
    private String phase;
    private String paymentMode;
    private String deliveryMode;
    private List<String> grades;
    private List<SchoolClz> clz;

    private enum PaymentMode{
        PAY_ONLINE,
        PAY_OFFLINE_SHOW_PRICE,
        PAY_OFFLINE_HIDE_PRICE
    }

    private enum DeliveryMode{
        HOME,
        SCHOOL
    }

    public static String guardPaymentMode(String paymentMode) {
        PaymentMode mode = PaymentMode.PAY_ONLINE;
        if(!StringUtils.isEmpty(paymentMode)) {
            try {
                mode = PaymentMode.valueOf(paymentMode);
            }catch(Exception ex) {
                logger.info("Invalid paymentMode {"+paymentMode+"} found. Use default one {"+mode.name()+"}");
            }
        }
        return mode.name();
    }

    public static String guardDeliveryMode(String deliveryMode) {
        DeliveryMode mode = DeliveryMode.HOME;
        if(!StringUtils.isEmpty(deliveryMode)) {
            try {
                mode = DeliveryMode.valueOf(deliveryMode);
            }catch (Exception ex) {
                logger.info("Invalid deliveryMode {"+deliveryMode+"} found. Use default one {"+mode.name()+"}");
            }
        }
        return mode.name();
    }

    public static String gradesToString(Set<String> grades) {
        StringBuilder sb = new StringBuilder();
        for(String s : grades) {
            if(!StringUtils.isEmpty(s)) {
                sb.append(s);
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public static School populate(SchoolInfo school) {
        return populate(school, -1);
    }

    public static School populate(SchoolInfo school, int numOfListings) {
        if(school == null) {
            return null;
        }
        School s = new School();
        s.setId(school.getId());
        s.setCode(school.getCode());
        s.setTitle(school.getTitle());
        s.setDispName(school.getDispName());
        s.setBgUrl(school.getBgUrl());
        s.setLogoUrl(school.getLogoUrl());
        s.setNumOfListings(numOfListings);
        s.setPhase(school.getPhase());
        s.setPaymentMode(guardPaymentMode(school.getPaymentMode()));
        s.setDeliveryMode(guardDeliveryMode(school.getDeliveryMode()));
        String grades = school.getGrades();
        if(!StringUtils.isEmpty(grades)) {
            s.setGrades(Arrays.asList(grades.split(";")));
        }
        if(school.getClzSet() != null) {
            List<SchoolClz> list = new ArrayList<>(school.getClzSet().size());
            for(SchoolClassInfo c : school.getClzSet()) {
                list.add(SchoolClz.populate(c));
            }
            s.setClz(list);
        }
        return s;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public String getDispName() {
        return this.dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public int getNumOfListings() {
        return this.numOfListings;
    }

    public void setNumOfListings(int numOfListings) {
        this.numOfListings = numOfListings;
    }

    public String getPhase() {
        return this.phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public List<String> getGrades() {
        return grades;
    }

    public void setGrades(List<String> grades) {
        this.grades = grades;
    }

    public List<SchoolClz> getClz() {
        return clz;
    }

    public void setClz(List<SchoolClz> clz) {
        this.clz = clz;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }
}
