package com.abuqool.sqool.vo;

import java.util.Date;

import com.abuqool.sqool.dao.common.OrderInfo;

public class Order {

    public static Order populate(OrderInfo order) {
        Order o = new Order();
        o.setOrderId(order.getOrderId());
        o.setPaymentTxId(order.getPaymentTxId());
        o.setTotalAmount(order.getTotalAmount());
        o.setConsigneeCity(order.getCity());
        o.setConsigneeDetails(order.getDetails());
        o.setConsigneeDistrict(order.getDistrict());
        o.setConsigneeName(order.getName());
        o.setConsigneePhone(order.getPhone());
        o.setConsigneeProvince(order.getProvince());
        o.setNotes(order.getNotes());
        o.setStatus(order.getStatus());
        o.setCreatedDate(order.getCreateTime());
        return o;
    }

    private String orderId;
    private String paymentTxId;
    private double totalAmount;
    private int status;
    private String notes;

    private String consigneeName;
    private String consigneePhone;
    private String consigneeProvince;
    private String consigneeCity;
    private String consigneeDistrict;
    private String consigneeDetails;

    private Date createdDate;

	public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getPaymentTxId() {
        return paymentTxId;
    }
    public void setPaymentTxId(String paymentTxId) {
        this.paymentTxId = paymentTxId;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getConsigneeName() {
        return consigneeName;
    }
    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }
    public String getConsigneePhone() {
        return consigneePhone;
    }
    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }
    public String getConsigneeProvince() {
        return consigneeProvince;
    }
    public void setConsigneeProvince(String consigneeProvince) {
        this.consigneeProvince = consigneeProvince;
    }
    public String getConsigneeCity() {
        return consigneeCity;
    }
    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }
    public String getConsigneeDistrict() {
        return consigneeDistrict;
    }
    public void setConsigneeDistrict(String consigneeDistrict) {
        this.consigneeDistrict = consigneeDistrict;
    }
    public String getConsigneeDetails() {
        return consigneeDetails;
    }
    public void setConsigneeDetails(String consigneeDetails) {
        this.consigneeDetails = consigneeDetails;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
