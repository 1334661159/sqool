package com.abuqool.sqool.vo;

import com.abuqool.sqool.dao.common.AbstractCustomValueProductInfo;

public class AbstractCustomValueProduct extends AbstractCustomKeyProduct{


    public void fillDAO(AbstractCustomValueProductInfo dao) {
        //using direct method call instead of reflection for bets performance

        super.fillDAO(dao);

        dao.setCustomFieldValue1(this.getCustomFieldValue1());
        dao.setCustomFieldValue2(this.getCustomFieldValue2());
        dao.setCustomFieldValue3(this.getCustomFieldValue3());
        dao.setCustomFieldValue4(this.getCustomFieldValue4());
        dao.setCustomFieldValue5(this.getCustomFieldValue5());
    }

    public void fromDAO(AbstractCustomValueProductInfo dao) {
        //using direct method call instead of reflection for bets performance
        super.fromDAO(dao);

        this.setCustomFieldValue1(dao.getCustomFieldValue1());
        this.setCustomFieldValue2(dao.getCustomFieldValue2());
        this.setCustomFieldValue3(dao.getCustomFieldValue3());
        this.setCustomFieldValue4(dao.getCustomFieldValue4());
        this.setCustomFieldValue5(dao.getCustomFieldValue5());
    }

    private String customFieldValue1;

    private String customFieldValue2;

    private String customFieldValue3;

    private String customFieldValue4;

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
