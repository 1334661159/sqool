package com.abuqool.sqool.dao.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractCustomValueProductInfo extends AbstractCustomKeyProductInfo {


    @Column(name="cf_val_1")
    private String customFieldValue1;

    @Column(name="cf_val_2")
    private String customFieldValue2;

    @Column(name="cf_val_3")
    private String customFieldValue3;

    @Column(name="cf_val_4")
    private String customFieldValue4;

    @Column(name="cf_val_5")
    private String customFieldValue5;

    public String getCustomFieldValue1() {
        return customFieldValue1;
    }

    public void setCustomFieldValue1(String customFieldValue1) {
        this.customFieldValue1 = customFieldValue1;
    }

    public String getCustomFieldValue2() {
        return customFieldValue2;
    }

    public void setCustomFieldValue2(String customFieldValue2) {
        this.customFieldValue2 = customFieldValue2;
    }

    public String getCustomFieldValue3() {
        return customFieldValue3;
    }

    public void setCustomFieldValue3(String customFieldValue3) {
        this.customFieldValue3 = customFieldValue3;
    }

    public String getCustomFieldValue4() {
        return customFieldValue4;
    }

    public void setCustomFieldValue4(String customFieldValue4) {
        this.customFieldValue4 = customFieldValue4;
    }

    public String getCustomFieldValue5() {
        return customFieldValue5;
    }

    public void setCustomFieldValue5(String customFieldValue5) {
        this.customFieldValue5 = customFieldValue5;
    }

}
