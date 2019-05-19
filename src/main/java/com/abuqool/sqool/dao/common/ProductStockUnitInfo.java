package com.abuqool.sqool.dao.common;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "sq_product_sku_info")
public class ProductStockUnitInfo extends AbstractProductUnitInfo{

    @ManyToOne
    @JoinColumn
    private ProductInfo product;

    @OneToMany(mappedBy = "sku", cascade = CascadeType.ALL)
    private Set<ProductOrderUnitInfo> orderSkuSet = new HashSet<>();

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public Set<ProductOrderUnitInfo> getOrderSkuSet() {
        return orderSkuSet;
    }

    public void setOrderSkuSet(Set<ProductOrderUnitInfo> orderSkuSet) {
        this.orderSkuSet = orderSkuSet;
    }

}
