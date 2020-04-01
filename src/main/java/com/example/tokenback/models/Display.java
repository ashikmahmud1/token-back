package com.example.tokenback.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "displays", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Display {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //display number
    @NotBlank
    private String name;

    private Integer from_queue;

    private Integer to_queue;

    // Relationship between department and display is one to many
    // Declare a field departments
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "display_departments",
            joinColumns = @JoinColumn(name = "display_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"))
    private Set<Department> departments = new HashSet<>();

    public Display() {
    }

    public Display(String name, Integer from_queue, Integer to_queue) {
        this.name = name;
        this.from_queue = from_queue;
        this.to_queue = to_queue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
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
