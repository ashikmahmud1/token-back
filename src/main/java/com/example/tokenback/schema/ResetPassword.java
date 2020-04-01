package com.example.tokenback.schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ResetPassword {
    @NotBlank
    @Size(min = 6, max = 40)
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
