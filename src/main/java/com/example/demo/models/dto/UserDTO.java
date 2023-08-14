package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public class UserDTO {

    private final BigInteger id;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;
    private final String phone;
    private final Set<RoleDTO> roles = new HashSet<>(1);

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }
}
