package com.globalmart.app.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.json.JSONObject;

import java.util.List;

public record UserRequest(@NotBlank(message = "username cannot be blank")
                          @Size(message = "invalid length for username", min = 2, max = 255)
                          String username,

                          @NotBlank(message = "password cannot be blank")
                          @Size(message = "minimum length for password is 14 characters", min = 14)
                          String password,

                          @NotBlank(message = "confirmPassword cannot be blank")
                          @Size(message = "minimum length for confirmPassword is 14 characters", min = 14)
                          String confirmPassword,

                          @NotBlank(message = "phone cannot be blank")
                          @Pattern(message = "invalid phone provided", regexp = "^(\\+)?\\d{11,15}$")
                          @Size(min = 11, max = 15, message = "invalid length for phone")
                          String phone,

                          @NotBlank(message = "email cannot be blank")
                          @Size(min = 5, message = "invalid length for email")
                          String email,

                          @NotBlank(message = "firstName cannot be blank")
                          @Size(message = "invalid length for firstName", min = 2, max = 255)
                          String firstName,

                          @NotBlank(message = "lastName cannot be blank")
                          @Size(message = "invalid length for lastName", min = 2, max = 255)
                          String lastName,

                          List<String> roleIds) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
