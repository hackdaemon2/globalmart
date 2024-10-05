package com.globalmart.app.services.impl;

import com.globalmart.app.dto.UserDTO;
import com.globalmart.app.dto.UserFilterDTO;
import com.globalmart.app.entity.UserEntity;
import com.globalmart.app.enums.ResponseCodes;
import com.globalmart.app.enums.SortOrder;
import com.globalmart.app.exception.ResourceConflictException;
import com.globalmart.app.exception.ResourceNotFoundException;
import com.globalmart.app.models.requests.UserRequest;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.repository.RoleRepository;
import com.globalmart.app.repository.UserRepository;
import com.globalmart.app.repository.UserSpecification;
import com.globalmart.app.services.UserService;
import com.globalmart.app.util.GenericUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.globalmart.app.models.constants.ApplicationConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @CachePut(value = "users", key = "#userRequest.username")
    public AppResponse<UserDTO> createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.username())) {
            throw new ResourceConflictException(USER_ACCOUNT_EXISTS);
        }
        UserEntity userEntity = this.mapToUserEntity(userRequest);
        UserDTO userDTO = GenericUtil.saveAndMapToDTO(userEntity, GenericUtil.toDTOFunction(UserDTO.class), userRepository);
        return GenericUtil.formulateSuccessAppResponse(userDTO);
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public AppResponse<UserDTO> updateUser(String id, UserRequest userRequest) {
        return userRepository.findByGuidAndDeletedIsFalse(id)
                             .map(existingUser -> this.updateUserEntityRoles(existingUser, userRequest))
                             .map(updatedUser -> GenericUtil.saveAndMapToDTO(
                                     updatedUser,
                                     GenericUtil.toDTOFunction(UserDTO.class),
                                     userRepository))
                             .map(GenericUtil::formulateSuccessAppResponse)
                             .orElseThrow(() -> new ResourceNotFoundException(USER_ACCOUNT_NOT_FOUND));
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public AppResponse<UserDTO> getUser(String id) {
        return userRepository.findByGuidAndDeletedIsFalse(id)
                             .map(user -> GenericUtil.mapToDTO(user, UserDTO.class))
                             .map(GenericUtil::formulateSuccessAppResponse)
                             .orElseThrow(() -> new ResourceNotFoundException(USER_ACCOUNT_NOT_FOUND));
    }

    @Override
    @Cacheable(value = "users", key = "'all'")
    public AppResponse<List<UserDTO>> getAllUser(long page, long size, SortOrder sortOrder,
                                                 UserFilterDTO userFilterDTO) {
        Pageable pageable = PageRequest.of(
                (int) page,
                (int) size,
                Sort.Direction.fromString(sortOrder.toString().toUpperCase()),
                DATE_CREATED);
        Page<UserEntity> userPage = userRepository.findAll(UserSpecification.withFilters(userFilterDTO), pageable);
        List<UserDTO> userDTOList = userPage.stream()
                                            .map(user -> GenericUtil.mapToDTO(user, UserDTO.class))
                                            .collect(Collectors.toList());
        return new AppResponse<>(
                (int) page,
                (int) size,
                (int) userPage.getTotalElements(),
                userDTOList,
                ResponseCodes.SUCCESS.getResponseCode(),
                ResponseCodes.SUCCESS.getResponseMessage());
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(String id) {
        userRepository.findByGuidAndDeletedIsFalse(id)
                      .map(userEntity -> {
                          userEntity.setDeleted(true);
                          return userRepository.save(userEntity);
                      })
                      .orElseThrow(() -> new ResourceNotFoundException(USER_ACCOUNT_NOT_FOUND));
    }

    private UserEntity mapToUserEntity(UserRequest userRequest) {
        return GenericUtil.mapToEntity(userRequest, UserEntity.class, (request, userEntity) -> {
            userEntity.setPassword(passwordEncoder.encode(request.password()));
            userEntity.setRoles(roleRepository.findByGuidInAndDeletedIsFalse(request.roleIds()));
        });
    }

    private UserEntity updateUserEntityRoles(UserEntity existingUserEntity, UserRequest userRequest) {
        MODEL_MAPPER.map(userRequest, existingUserEntity);
        existingUserEntity.setRoles(roleRepository.findByGuidInAndDeletedIsFalse(userRequest.roleIds()));
        return existingUserEntity;
    }

}
