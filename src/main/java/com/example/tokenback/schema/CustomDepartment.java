package com.example.tokenback.schema;

public class CustomDepartment {
    private String name;
    private String letter;
    private Integer start_number;
    private String color;
    private Long display_id;

    public CustomDepartment() {
    }

    public CustomDepartment(String name, String letter, Integer start_number, String color, Long display_id) {
        this.name = name;
        this.letter = letter;
        this.start_number = start_number;
        this.color = color;
        this.display_id = display_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Integer getStart_number() {
        return start_number;
    }

    public void setStart_number(Integer start_number) {
        this.start_number = start_number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getDisplay_id() {
        return display_id;
    }

    public void setDisplay_id(Long display_id) {
        this.display_id = display_id;
    }
}
