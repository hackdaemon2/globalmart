package com.globalmart.app.models.responses;

import org.json.JSONObject;

public record ErrorResponse(String responseCode,
                            String responseMessage) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
