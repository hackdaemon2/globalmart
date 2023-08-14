package com.example.demo.models.responses;

import com.example.demo.models.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserResponse {

    private final String responseCode;
    private final String responseMessage;
    private final List<UserDTO> data;
    private final Integer rowCount;
    private final Integer page;
    private final Integer size;
}
