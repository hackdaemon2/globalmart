package com.globalmart.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public record UserDTO(@JsonProperty("id") String guid,
                      String firstName,
                      String lastName,
                      String username,
                      String email,
                      String phone,
                      Set<RoleDTO> roles) {
    public UserDTO {
        roles = roles == null ? new HashSet<>(1) : new HashSet<>(roles);
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
