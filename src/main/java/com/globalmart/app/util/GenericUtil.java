package com.globalmart.app.util;

import com.globalmart.app.enums.ResponseCodes;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.repository.SpecificationRepository;
import jakarta.transaction.Transactional;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.globalmart.app.models.constants.ApplicationConstants.MODEL_MAPPER;

public final class GenericUtil {

    private GenericUtil() {
        throw new IllegalStateException(GenericUtil.class.getName());
    }

    public static <T, R> R mapToEntity(T request, Class<R> entityClass,
                                       BiConsumer<T, R> customMapping) {
        R entity = MODEL_MAPPER.map(request, entityClass);
        customMapping.accept(request, entity);
        return entity;
    }

    @Transactional
    public static <P, R, D> R saveAndMapToDTO(P entity, Function<P, R> mapper,
                                              SpecificationRepository<P, D> repository) {
        repository.save(entity);
        return mapper.apply(entity);
    }

    public static <T> AppResponse<T> formulateSuccessAppResponse(T data) {
        return new AppResponse<>(
                null,
                null,
                null,
                data,
                ResponseCodes.SUCCESS.getResponseCode(),
                ResponseCodes.SUCCESS.getResponseMessage());
    }

    public static <E, R> R mapToDTO(E entity, Class<R> clazz) {
        return MODEL_MAPPER.map(entity, clazz);
    }

    public static <E, R> Function<E, R> toDTOFunction(Class<R> dtoClass) {
        return entity -> mapToDTO(entity, dtoClass);
    }

}
