package com.example.tokenback.schema;

import java.util.Set;

public class CustomDisplay {

    private Integer number;

    private Set<Long> departments;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Set<Long> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Long> departments) {
        this.departments = departments;
    }
}
