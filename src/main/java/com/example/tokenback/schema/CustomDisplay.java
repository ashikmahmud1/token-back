package com.example.tokenback.schema;

import java.util.Set;

public class CustomDisplay {

    private String name;

    private Integer from_queue;

    private Integer to_queue;

    private Set<Long> departments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Long> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Long> departments) {
        this.departments = departments;
    }

    public Integer getFrom_queue() {
        return from_queue;
    }

    public void setFrom_queue(Integer from_queue) {
        this.from_queue = from_queue;
    }

    public Integer getTo_queue() {
        return to_queue;
    }

    public void setTo_queue(Integer to_queue) {
        this.to_queue = to_queue;
    }
}
