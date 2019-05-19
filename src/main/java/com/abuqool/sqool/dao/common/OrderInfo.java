package com.abuqool.sqool.dao.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "sq_order_info")
public class OrderInfo extends AbstractConsigneeInfo{

    public static final int ORDER_STATUS_CREATED = 1;
    public static final int ORDER_STATUS_PAID = 2;
    public static final int ORDER_STATUS_SHIPPED = 3;
    public static final int ORDER_STATUS_DELIVERED = 4;
    public static final int ORDER_STATUS_CANCELLED = -1;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "payment_tx_id")
    private String paymentTxId;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "status")
    private int status;

    @Column(name = "notes")
    private String notes;

    @Column(name="clz")
    private String clz;

    @Column(name="grade")
    private String grade;

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//    private Set<ProductOrderUnitInfo> puSet = new HashSet<>();

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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
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

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

//    public Set<ProductOrderUnitInfo> getPuSet() {
//        return puSet;
//    }
//
//    public void setPuSet(Set<ProductOrderUnitInfo> puSet) {
//        this.puSet = puSet;
//    }

}