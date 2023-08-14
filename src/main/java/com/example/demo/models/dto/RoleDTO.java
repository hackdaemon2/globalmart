package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class RoleDTO {

    private final BigInteger id;
    private final String name;
}
