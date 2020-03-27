package com.example.tokenback.schema;

import java.util.Set;

public class CustomUser {

    private String name;

    private String username;

    private String email;

    private Set<String> role;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRole() {
        return role;
    }
}
