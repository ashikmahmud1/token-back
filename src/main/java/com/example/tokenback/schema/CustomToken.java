package com.example.tokenback.schema;

import com.example.tokenback.models.ETokenStatus;

import java.lang.reflect.Field;
import java.util.Date;

public class CustomToken {

    private String token_number;

    private Date serving_start;

    private Date serving_end;

    private Boolean priority;

    private Long user_id;

    private Long customer_id;

    private Long department_id;

    private Long counter_id;

    private ETokenStatus status;
    private String type;

    public CustomToken() {
    }

    public ETokenStatus getStatus() {
        return status;
    }

    public void setStatus(ETokenStatus status) {
        this.status = status;
    }

    public String getToken_number() {
        return token_number;
    }

    public void setToken_number(String token_number) {
        this.token_number = token_number;
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

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public Long getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Long department_id) {
        this.department_id = department_id;
    }

    public Long getCounter_id() {
        return counter_id;
    }

    public void setCounter_id(Long counter_id) {
        this.counter_id = counter_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append( this.getClass().getName() );
        result.append( " Object {" );
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for ( Field field : fields  ) {
            result.append("  ");
            try {
                result.append( field.getName() );
                result.append(": ");
                //requires access to private field:
                result.append( field.get(this) );
            } catch ( IllegalAccessException ex ) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
