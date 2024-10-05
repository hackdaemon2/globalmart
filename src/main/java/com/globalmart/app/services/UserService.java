package com.globalmart.app.services;

import com.globalmart.app.dto.UserDTO;
import com.globalmart.app.dto.UserFilterDTO;
import com.globalmart.app.enums.SortOrder;
import com.globalmart.app.models.requests.UserRequest;
import com.globalmart.app.models.responses.AppResponse;

import java.util.List;

public interface UserService {

    AppResponse<UserDTO> createUser(UserRequest userRequest);

    AppResponse<UserDTO> updateUser(String id, UserRequest userRequest);

    AppResponse<UserDTO> getUser(String id);

    AppResponse<List<UserDTO>> getAllUser(long page, long size, SortOrder sortOrder, UserFilterDTO filter);

    void deleteUser(String id);

}
