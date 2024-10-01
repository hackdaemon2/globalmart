package com.globalmart.app.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.JSONObject;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppResponse<T>(Integer page,
                             Integer size,
                             Integer rowCount,
                             T data,
                             String responseCode,
                             String responseMessage) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
