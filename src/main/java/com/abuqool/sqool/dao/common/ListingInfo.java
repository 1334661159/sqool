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
@Table(name = "sq_listing_info")
public class ListingInfo extends AbstractSchoolSubInfo{
    @Column(name = "list_price")
    private double listPrice;

    @Column(name = "productId")
    private int productId;

    @Column(name = "status")
    private int status;

    @Column(name = "sku_pattern")
    private String skuPattern;

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSkuPattern() {
        return skuPattern;
    }

    public void setSkuPattern(String skuPattern) {
        this.skuPattern = skuPattern;
    }
}
