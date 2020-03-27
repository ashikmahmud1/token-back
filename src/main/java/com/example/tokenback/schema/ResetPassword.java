package com.example.tokenback.schema;

public class ResetPassword {
    private String password;
    private String confirmPassword;
    private String oldPassword;

    public String getPassword() {
        return password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
