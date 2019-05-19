package com.abuqool.sqool.dao.common;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "sq_product_info")
@EqualsAndHashCode(exclude = "skuSet,picSet")
public class ProductInfo extends AbstractCustomKeyProductInfo {

    @Column(name = "ranking")
    private Integer ranking;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductStockUnitInfo> skuSet = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductPicInfo> picSet = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "sq_products_schools",
            joinColumns = { @JoinColumn(name = "product_id") },
            inverseJoinColumns = { @JoinColumn(name = "school_id") })
    private Set<SchoolInfo> schools = new HashSet<>();

    @Column(name="color_options")
    private String colorOptions;

    @Column(name="size_options")
    private String sizeOptions;

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Set<ProductStockUnitInfo> getSkuSet() {
        return skuSet;
    }

    public void setSkuSet(Set<ProductStockUnitInfo> skuSet) {
        this.skuSet = skuSet;
    }

    public Set<ProductPicInfo> getPicSet() {
        return picSet;
    }

    public void setPicSet(Set<ProductPicInfo> picSet) {
        this.picSet = picSet;
    }

    public Set<SchoolInfo> getSchools() {
        return schools;
    }

    public void setSchools(Set<SchoolInfo> schools) {
        this.schools = schools;
    }

    public String getColorOptions() {
        return colorOptions;
    }

    public void setColorOptions(String colorOptions) {
        this.colorOptions = colorOptions;
    }

    public String getSizeOptions() {
        return sizeOptions;
    }

    public void setSizeOptions(String sizeOptions) {
        this.sizeOptions = sizeOptions;
    }
}
