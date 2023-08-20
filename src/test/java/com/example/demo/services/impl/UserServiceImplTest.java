package com.example.demo.services.impl;

import com.example.demo.configurations.ApplicationContextProvider;
import com.example.demo.models.requests.UserRequest;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@RequiredArgsConstructor
@SpringBootTest(classes = ApplicationContextProvider.class)
class UserServiceImplTest {

    private final UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        List<BigInteger> roleIds = new ArrayList<>(1);
        this.userRequest = new UserRequest(
                "hackdaemon",
                "xyzM#$%&@6999_______",
                "xyzM#$%&@6999_______",
                "2348030000000",
                "hackdaemon@mail.com",
                "John",
                "Doe",
                roleIds);
    }

    @AfterEach
    void tearDown() {
        this.userRequest = null;
    }

    @Test
    void createUser() {
        Mockito.when(userRepository.getUserDetails(anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(null);
        Mockito.when(roleRepository.findByIdInAndDeletedIsFalse(anyList())).thenReturn(Collections.emptySet());
        var response = userService.createUser(this.userRequest);
        assertEquals("00", response.getResponseCode());
    }

    @Test
    void updateUser() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getAllUser() {
    }

    @Test
    void deleteUser() {
    }
}