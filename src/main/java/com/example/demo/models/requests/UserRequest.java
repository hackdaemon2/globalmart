package com.example.demo.models.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "username cannot be blank")
    @NotEmpty(message = "username cannot be empty")
    @NotNull(message = "username cannot be null")
    @Size(message = "invalid length for username", min = 2, max = 255)
    private final String username;

    @NotBlank(message = "password cannot be blank")
    @NotEmpty(message = "password cannot be empty")
    @NotNull(message = "password cannot be null")
    @Size(message = "minimum length for password is 14 characters", min = 14)
    private final String password;

    @NotBlank(message = "confirmPassword cannot be blank")
    @NotEmpty(message = "confirmPassword cannot be empty")
    @NotNull(message = "confirmPassword cannot be null")
    @Size(message = "minimum length for confirmPassword is 14 characters", min = 14)
    private final String confirmPassword;

    @NotBlank(message = "phone cannot be blank")
    @NotEmpty(message = "phone cannot be empty")
    @NotNull(message = "phone cannot be null")
    @Pattern(message = "invalid phone provided", regexp = "^(\\+)?\\d{11,15}$")
    @Size(min = 11, max = 15, message = "invalid length for phone")
    private final String phone;

    @NotBlank(message = "email cannot be blank")
    @NotEmpty(message = "email cannot be empty")
    @NotNull(message = "email cannot be null")
    @Size(min = 5, message = "invalid length for email")
    private final String email;

    @NotBlank(message = "firstName cannot be blank")
    @NotEmpty(message = "firstName cannot be empty")
    @NotNull(message = "firstName cannot be null")
    @Size(message = "invalid length for firstName", min = 2, max = 255)
    @Size(min = 2, message = "invalid length for firstName")
    private final String firstName;

    @NotBlank(message = "lastName cannot be blank")
    @NotEmpty(message = "lastName cannot be empty")
    @NotNull(message = "lastName cannot be null")
    @Size(min = 2, message = "invalid length for lastName")
    @Size(message = "invalid length for lastName", min = 2, max = 255)
    private final String lastName;

    private final List<BigInteger> roleIds = new ArrayList<>(1);

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }
}
