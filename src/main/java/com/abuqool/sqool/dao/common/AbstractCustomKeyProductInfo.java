package com.abuqool.sqool.dao.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractCustomKeyProductInfo extends AbstractProductInfo {

    @Column(name="cf_key_1")
    private String customFieldKey1;

    @Column(name="cf_key_2")
    private String customFieldKey2;

    @Column(name="cf_key_3")
    private String customFieldKey3;

    @Column(name="cf_key_4")
    private String customFieldKey4;

    @Column(name="cf_key_5")
    private String customFieldKey5;


    public String getCustomFieldKey1() {
        return customFieldKey1;
    }

    public void setCustomFieldKey1(String customFieldKey1) {
        this.customFieldKey1 = customFieldKey1;
    }

    public String getCustomFieldKey2() {
        return customFieldKey2;
    }

    public void setCustomFieldKey2(String customFieldKey2) {
        this.customFieldKey2 = customFieldKey2;
    }

    public String getCustomFieldKey3() {
        return customFieldKey3;
    }

    public void setCustomFieldKey3(String customFieldKey3) {
        this.customFieldKey3 = customFieldKey3;
    }

    public String getCustomFieldKey4() {
        return customFieldKey4;
    }

    public void setCustomFieldKey4(String customFieldKey4) {
        this.customFieldKey4 = customFieldKey4;
    }

    public String getCustomFieldKey5() {
        return customFieldKey5;
    }

    public void setCustomFieldKey5(String customFieldKey5) {
        this.customFieldKey5 = customFieldKey5;
    }

}
