package com.abuqool.sqool.dao.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "sq_order_product_info")
public class ProductOrderUnitInfo extends AbstractProductUnitInfo{

    @ManyToOne
    @JoinColumn
    private ProductInfo product;

    @ManyToOne
    @JoinColumn
    private ProductStockUnitInfo sku;

//    @ManyToOne
//    @JoinColumn
//    private OrderInfo order;

    @Column(name="order_id")
    private int orderId;

    @Column(name="code")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public ProductStockUnitInfo getSku() {
        return sku;
    }

    public void setSku(ProductStockUnitInfo sku) {
        this.sku = sku;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

//    public OrderInfo getOrder() {
//        return order;
//    }
//
//    public void setOrder(OrderInfo order) {
//        this.order = order;
//    }

}