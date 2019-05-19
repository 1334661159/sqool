package com.abuqool.sqool.dao.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class AbstractProductInfo extends AbstractInfo{

    @Column(name="status")
    private int status;

    @Column(name = "title")
    private String title;

    @Column(name = "category")
    private String category;

    @Column(name = "gender")
    private String gender;

    @Column(name = "price")
    private Double price;

    @Column(name = "mkt_price")
    private Double mktPrice;

    @Column(name = "pic_url")
    private String picUrl;

    @Column(name="code", unique=true)
    private String code;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getMktPrice() {
        return mktPrice;
    }

    public void setMktPrice(Double mktPrice) {
        this.mktPrice = mktPrice;
    }

}