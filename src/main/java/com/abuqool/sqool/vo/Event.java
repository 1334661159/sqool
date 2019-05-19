package com.abuqool.sqool.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.abuqool.sqool.dao.common.EventInfo;
import com.abuqool.sqool.dao.common.EventPhaseInfo;

public class Event {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String fromDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Date toDate(String str) {
        try {
            return DATE_FORMAT.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Event populate(EventInfo event, EventPhaseInfo presale, EventPhaseInfo shipping) {
        Event e = new Event();
        e.setTitle(event.getTitle());
        e.setPreSaleStart(presale.getStartTime());
        e.setPreSaleEnd(presale.getEndTime());
        e.setShippingStart(shipping.getStartTime());
        e.setShippingEnd(shipping.getEndTime());
        e.setPaymentMode(event.getPaymentMode());
        e.setDeliveryMode(event.getDeliveryMode());
        return e;
    }

    private String title;
    private String preSaleStart;
    private String preSaleEnd;
    private String shippingStart;
    private String shippingEnd;
    private String paymentMode;
    private String deliveryMode;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPreSaleStart() {
        return preSaleStart;
    }
    public void setPreSaleStart(Date preSaleStart) {
        this.preSaleStart = fromDate(preSaleStart);
    }
    public String getPreSaleEnd() {
        return preSaleEnd;
    }
    public void setPreSaleEnd(Date preSaleEnd) {
        this.preSaleEnd = fromDate(preSaleEnd);
    }
    public String getShippingStart() {
        return shippingStart;
    }
    public void setShippingStart(Date shippingStart) {
        this.shippingStart = fromDate(shippingStart);
    }
    public String getShippingEnd() {
        return shippingEnd;
    }
    public void setShippingEnd(Date shippingEnd) {
        this.shippingEnd = fromDate(shippingEnd);
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
