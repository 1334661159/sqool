package com.abuqool.sqool.vo;

import com.abuqool.sqool.dao.common.SchoolClassInfo;

public class SchoolClz {
    public static SchoolClz populate(SchoolClassInfo c) {
        SchoolClz sc = new SchoolClz();
        sc.setId(c.getId());
        sc.setGrade(c.getGrade());
        sc.setTitle(c.getTitle());
        return sc;
    }

    private int id;
    private String title;
    private String grade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
