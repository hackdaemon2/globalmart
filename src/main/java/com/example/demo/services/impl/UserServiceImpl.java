package com.example.demo.services.impl;

import com.example.demo.enums.ResponseCodes;
import com.example.demo.enums.SortOrder;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.dto.UserDTO;
import com.example.demo.models.entities.RoleEntity;
import com.example.demo.models.entities.UserEntity;
import com.example.demo.models.requests.UserRequest;
import com.example.demo.models.responses.UserResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.models.constants.ApplicationConstants.USER_ACCOUNT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        Optional<UserEntity> userDetails = userRepository.getUserDetails(userRequest.getUsername());

        userDetails.ifPresent(user -> {
            throw new ResourceConflictException("username already exists");
        });

        Set<RoleEntity> roles = roleRepository.findByIdInAndDeletedIsFalse(userRequest.getRoleIds());
        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(userRequest.getUsername());
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setPhone(userRequest.getPhone());
        userEntity.setRoles(roles);

        userRepository.save(userEntity);

        var userDto = UserServiceUtility.mapToUserDTO(userEntity);
        var successResponses = ResponseCodes.SUCCESS;

        return UserServiceUtility.formulateUserResponse(successResponses, userDto);
    }

    @Override
    public UserResponse updateUser(BigInteger id, UserRequest userRequest) {
        UserEntity existingUserEntity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_ACCOUNT_NOT_FOUND));
        Set<RoleEntity> roles = roleRepository.findByIdInAndDeletedIsFalse(userRequest.getRoleIds());

        existingUserEntity.setFirstName(userRequest.getFirstName());
        existingUserEntity.setLastName(userRequest.getLastName());
        existingUserEntity.setEmail(userRequest.getEmail());
        existingUserEntity.setPhone(userRequest.getPhone());
        existingUserEntity.setRoles(roles);

        userRepository.save(existingUserEntity);

        UserDTO userDto = UserServiceUtility.mapToUserDTO(existingUserEntity);
        ResponseCodes successResponses = ResponseCodes.SUCCESS;

        return UserServiceUtility.formulateUserResponse(successResponses, userDto);
    }

    @Override
    public UserResponse getUser(BigInteger id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_ACCOUNT_NOT_FOUND));
        UserDTO userDto = UserServiceUtility.mapToUserDTO(userEntity);
        ResponseCodes successResponses = ResponseCodes.SUCCESS;
        return UserServiceUtility.formulateUserResponse(successResponses, userDto);
    }

    @Override
    public UserResponse getAllUser(BigInteger page, BigInteger size, String sortOrder) {
        try {
            SortOrder sort = SortOrder.valueOf(sortOrder.toLowerCase());
            Sort.Direction direction = Sort.Direction.DESC;

            if (sort == SortOrder.ASC) {
                direction = Sort.Direction.ASC;
            }

            Page<UserEntity> userPage = userRepository.findAll(PageRequest.of(page.intValue(), size.intValue(), direction, "dateCreated"));
            List<UserEntity> userList = userPage.getContent();
            List<UserDTO> userDTOList = userList.stream().map(UserServiceUtility::mapToUserDTO).collect(Collectors.toList());
            ResponseCodes successResponses = ResponseCodes.SUCCESS;
            int rowCount = Integer.parseInt(String.valueOf(userPage.getTotalElements()));
            return UserServiceUtility.formulateUserResponse(successResponses, userDTOList, rowCount, page.intValue(), size.intValue());
        } catch (IllegalArgumentException exception) {
            return UserServiceUtility.formulateUserResponse(ResponseCodes.FAILURE, null);
        }
    }

    @Override
    public void deleteUser(BigInteger id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);

        if (Objects.isNull(userEntity)) {
            throw new ResourceNotFoundException(USER_ACCOUNT_NOT_FOUND);
        }

        userEntity.setDeleted(true);
        userRepository.save(userEntity);
    }

    private static class UserServiceUtility {

        private UserServiceUtility() {
            throw new IllegalStateException();
        }

        private static UserDTO mapToUserDTO(UserEntity userEntity) {
            return new ModelMapper().map(userEntity, UserDTO.class);
        }

        private static UserResponse formulateUserResponse(ResponseCodes responseCodes, UserDTO userDTO) {
            return new UserResponse(
                    responseCodes.getResponseCode(),
                    responseCodes.getResponseMessage(),
                    Collections.singletonList(userDTO),
                    null,
                    null,
                    null);
        }

        private static UserResponse formulateUserResponse(ResponseCodes responseCodes, List<UserDTO> userDTO,
                                                          Integer rowCount, Integer page, Integer size) {
            return new UserResponse(
                    responseCodes.getResponseCode(),
                    responseCodes.getResponseMessage(),
                    userDTO,
                    rowCount,
                    page,
                    size);
        }
    }
}
