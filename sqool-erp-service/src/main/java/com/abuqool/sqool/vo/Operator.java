package com.abuqool.sqool.vo;

import com.abuqool.sqool.dao.mgmt.OperatorInfo;

public class Operator {

    public static Operator populate(OperatorInfo operator) {
        Operator o = new Operator();
        o.setNickname(operator.getNickname());
        o.setName(operator.getName());
        o.setPhone(operator.getPhone());
        o.setToken(operator.getSessionToken());
        o.setWechat(operator.getWechat());
        o.setCompany(operator.getCompany());
        o.setActivated(operator.isActivated());
        return o;
    }

    private String token;
    private String name;
    private String nickname;
    private String phone;
    private String wechat;
    private String company;
    private boolean activated;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getWechat() {
        return wechat;
    }
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
    public boolean isActivated() {
        return activated;
    }
    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
