package com.example.tokenback.schema;

import com.example.tokenback.models.Token;
import com.example.tokenback.models.User;

import java.util.List;

public class UserReport {

    List<User> users;

    List<Token> tokens;

    public UserReport(List<User> users, List<Token> tokens) {
        this.users = users;
        this.tokens = tokens;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
