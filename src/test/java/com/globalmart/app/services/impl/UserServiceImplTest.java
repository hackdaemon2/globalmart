package com.globalmart.app.services.impl;

import com.globalmart.app.models.requests.UserRequest;
import com.globalmart.app.repository.RoleRepository;
import com.globalmart.app.repository.UserRepository;
import com.globalmart.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@SpringBootTest
class UserServiceImplTest {

    private final UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        List<String> roleIds = new ArrayList<>(1);
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