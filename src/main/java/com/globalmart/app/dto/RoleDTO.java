package com.globalmart.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.json.JSONObject;

@Getter
public record RoleDTO(@JsonProperty("id") String guid, String name) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
