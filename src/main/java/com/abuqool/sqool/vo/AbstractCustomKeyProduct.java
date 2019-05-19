package com.abuqool.sqool.vo;

import com.abuqool.sqool.dao.common.AbstractCustomKeyProductInfo;

public class AbstractCustomKeyProduct {

    public void fillDAO(AbstractCustomKeyProductInfo dao) {
        //using direct method call instead of reflection for bets performance

        dao.setCustomFieldKey1(this.getCustomFieldKey1());
        dao.setCustomFieldKey2(this.getCustomFieldKey2());
        dao.setCustomFieldKey3(this.getCustomFieldKey3());
        dao.setCustomFieldKey4(this.getCustomFieldKey4());
        dao.setCustomFieldKey5(this.getCustomFieldKey5());
    }

    public void fromDAO(AbstractCustomKeyProductInfo dao) {
        //using direct method call instead of reflection for bets performance

        this.setCustomFieldKey1(dao.getCustomFieldKey1());
        this.setCustomFieldKey2(dao.getCustomFieldKey2());
        this.setCustomFieldKey3(dao.getCustomFieldKey3());
        this.setCustomFieldKey4(dao.getCustomFieldKey4());
        this.setCustomFieldKey5(dao.getCustomFieldKey5());
    }

    public static void fillVO(AbstractCustomKeyProductInfo dao, AbstractCustomKeyProduct vo) {
        
    }

    private String customFieldKey1;

    private String customFieldKey2;

    private String customFieldKey3;

    private String customFieldKey4;

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
