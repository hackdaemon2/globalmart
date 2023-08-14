package com.example.demo.services;

import com.example.demo.models.requests.UserRequest;
import com.example.demo.models.responses.UserResponse;

import java.math.BigInteger;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(BigInteger id, UserRequest userRequest);

    UserResponse getUser(BigInteger id);

    UserResponse getAllUser(BigInteger page, BigInteger size, String sortOrder);

    void deleteUser(BigInteger id);
}
