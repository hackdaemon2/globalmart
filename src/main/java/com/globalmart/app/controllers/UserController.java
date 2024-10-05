package com.globalmart.app.controllers;

import com.globalmart.app.dto.UserDTO;
import com.globalmart.app.models.requests.UserRequest;
import com.globalmart.app.models.requests.UserRequestParam;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.globalmart.app.models.constants.SwaggerConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create User", description = "Create a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_CREATED_STATUS, description = "Created"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    public ResponseEntity<AppResponse<UserDTO>> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(userService.createUser(userRequest));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Update User", description = "Update a user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_OK_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<UserDTO>> updateUser(@NotNull @PathVariable String id,
                                                           @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Delete User", description = "Delete a user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_NO_CONTENT_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<Void> deleteUser(@NotNull @PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Get Single User", description = "Get a user's details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_OK_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<UserDTO>> getUser(@NotNull @PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Get All Users", description = "Get all users details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_OK_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<List<UserDTO>>> getAllUser(UserRequestParam userRequestParam) {
        return ResponseEntity.ok(userService.getAllUser(
                userRequestParam.page(),
                userRequestParam.size(),
                userRequestParam.sortOrder(),
                userRequestParam.filter()));
    }

}
