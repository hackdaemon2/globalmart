package com.example.demo.utils;

import com.example.demo.exception.GenericApplicationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@SuppressWarnings("unused")
public final class JsonMapper {

    private JsonMapper() {
        throw new IllegalStateException();
    }

    public static <T> String toJson(T object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            throw new GenericApplicationException(exception.getMessage());
        }
    }

    public static boolean isValidJson(String json) {
        try {
            new ObjectMapper().readTree(StringUtils.isNotEmpty(json) ? json : "<div>/<div>");
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException exception) {
            throw new GenericApplicationException(exception.getMessage());
        }
    }

    public static ObjectNode createObjectNode() {
        return new ObjectMapper().createObjectNode();
    }

    public static <T> Map<String, Object> toMap(T type) {
        try {
            return new ObjectMapper().convertValue(type, new TypeReference<>() {
            });
        } catch (IllegalArgumentException exception) {
            throw new GenericApplicationException(exception.getMessage());
        }
    }

    public static <T, R> R toType(T source, Class<R> target) {
        try {
            return new ObjectMapper().convertValue(source, target);
        } catch (IllegalArgumentException exception) {
            throw new GenericApplicationException(exception.getMessage());
        }
    }

}
