package com.example.tokenback.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "tokens")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Token Number (String field)
    @NotBlank
    private String token_number;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @CreatedDate
    private Date createdAt = new Date(); // initialize created date

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @LastModifiedDate
    private Date updatedAt = new Date(); // initialize updated date

    // Serving Start
    private Date serving_start;

    // Serving End
    private Date serving_end;

    private Boolean priority;

    // We have to keep field user_id, customer_id, department_id and counter_id

    // User (Relational field) Which user called this token
    // Here we should not do the lazy loading data
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // Customer (Relational field) Token generated for the customer
    // Here we should not do the lazy loading data
    @OneToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Department (Relational field) Under which department token added
    // Here we should not do the lazy loading data
    // If we remove optional than the @ManyToOne() loading will be recursive
    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // Counter (Relational field) Under which counter token assigned
    // Here we should not do the lazy loading data
    @ManyToOne(optional = false)
    @JoinColumn(name = "counter_id")
    private Counter counter;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ETokenStatus status;

    public Token() {
    }

    public Token(String token_number, Customer customer, Department department, Boolean priority, ETokenStatus status) {
        this.token_number = token_number;
        this.customer = customer;
        this.priority = priority;
        this.department = department;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken_number() {
        return token_number;
    }

    public void setToken_number(String token_number) {
        this.token_number = token_number;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getServing_start() {
        return serving_start;
    }

    public void setServing_start(Date serving_start) {
        this.serving_start = serving_start;
    }

    public Date getServing_end() {
        return serving_end;
    }

    public void setServing_end(Date serving_end) {
        this.serving_end = serving_end;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }

    public ETokenStatus getStatus() {
        return status;
    }

    public void setStatus(ETokenStatus status) {
        this.status = status;
    }

}
