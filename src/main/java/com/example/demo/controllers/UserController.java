package com.example.demo.controllers;

import com.example.demo.models.requests.UserRequest;
import com.example.demo.models.responses.UserResponse;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@NotNull @PathVariable BigInteger id,
                                                   @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@NotNull @PathVariable BigInteger id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@NotNull @PathVariable BigInteger id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<UserResponse> getAllUser(@Min(value = 0) @RequestParam(defaultValue = "0") BigInteger page,
                                                   @RequestParam(defaultValue = "50") BigInteger size,
                                                   @NotNull(message = "sortOrder cannot be null")
                                                   @NotEmpty(message = "sortOrder cannot be empty")
                                                   @NotBlank(message = "sortOrder cannot be blank") String sortOrder) {
        return ResponseEntity.ok(userService.getAllUser(page, size, sortOrder));
    }

}
