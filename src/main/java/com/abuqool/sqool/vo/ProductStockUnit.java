package com.abuqool.sqool.vo;

import com.abuqool.sqool.dao.common.ProductStockUnitInfo;

public class ProductStockUnit extends AbstractCustomValueProduct {
    private int id;
    private String code;
    private int quantity;
    private String picUrl;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static ProductStockUnit populate(ProductStockUnitInfo s) {
        ProductStockUnit u = new ProductStockUnit();
        u.setId(s.getId());
        u.setCode(s.getCode());
        u.setQuantity(s.getQuantity());
        u.fromDAO(s);
        return u;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
