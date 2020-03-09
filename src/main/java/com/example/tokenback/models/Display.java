package com.example.tokenback.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "displays", uniqueConstraints = {
        @UniqueConstraint(columnNames = "number")
})
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Display {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //display number
    private Integer number;

    // Relationship between department and display is one to many
    // Declare a field departments
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "display_departments",
            joinColumns = @JoinColumn(name = "display_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"))
    private Set<Department> departments = new HashSet<>();

    public Display() {
    }

    public Display(Integer number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }
}
