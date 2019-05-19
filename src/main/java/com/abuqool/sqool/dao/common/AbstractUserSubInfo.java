package com.abuqool.sqool.dao.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractUserSubInfo extends AbstractSchoolSubInfo {

    @Column(name = "user_id")
    protected int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
