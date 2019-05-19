package com.abuqool.sqool.vo;

import com.abuqool.sqool.dao.common.ProductOrderUnitInfo;

public class ProductOrderUnit extends ProductStockUnit {

    public static ProductOrderUnit populate(ProductOrderUnitInfo info) {
        ProductOrderUnit u = new ProductOrderUnit();
        u.setCode(info.getCode());
        u.setQuantity(info.getQuantity());
        u.setTitle(info.getTitle());
        u.setPicUrl(info.getPicUrl());
        u.setCategory(info.getCategory());
        u.setPrice(info.getPrice());
        u.fromDAO(info);
        return u;
    }

    private String title;
    private String picUrl;
    private String category;
    private Double price;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPicUrl() {
        return picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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
}
