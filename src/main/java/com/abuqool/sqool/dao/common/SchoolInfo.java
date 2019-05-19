
package com.abuqool.sqool.dao.common;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@Table(name = "sq_school_info")
public class SchoolInfo extends AbstractInfo {
    @Column(name = "title")
    private String title;

    @Column(name = "code")
    private String code;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "bg_url")
    private String bgUrl;

    @Column(name = "status")
    private int status;

    @Column(name = "disp_name")
    private String dispName;

    @Column(name = "phase")
    private String phase;

    @Column(name = "grades")
    private String grades;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "delivery_mode")
    private String deliveryMode;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    private Set<SchoolClassInfo> clzSet = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "schools")
    private Set<ProductInfo> products = new HashSet<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<ProductInfo> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductInfo> products) {
        this.products = products;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public Set<SchoolClassInfo> getClzSet() {
        return clzSet;
    }

    public void setClzSet(Set<SchoolClassInfo> clzSet) {
        this.clzSet = clzSet;
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
