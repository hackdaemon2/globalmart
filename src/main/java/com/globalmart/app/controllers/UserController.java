package com.globalmart.app.controllers;

import com.globalmart.app.dto.UserDTO;
import com.globalmart.app.dto.UserFilterDTO;
import com.globalmart.app.models.requests.UserRequest;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<AppResponse<UserDTO>> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(userService.createUser(userRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse<UserDTO>> updateUser(@NotNull @PathVariable String id,
                                                           @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(userService.updateUser(id, userRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@NotNull @PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<UserDTO>> getUser(@NotNull @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<AppResponse<List<UserDTO>>> getAllUser(@Min(value = 0) @RequestParam(defaultValue = "0") long page,
                                                                 @RequestParam(defaultValue = "50") long size,
                                                                 @NotBlank(message = "sortOrder cannot be null") String sortOrder,
                                                                 @RequestParam UserFilterDTO filter) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(userService.getAllUser(page, size, sortOrder, filter));
    }

}
