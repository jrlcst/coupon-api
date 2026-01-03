package com.jrlcst.couponapi.shared.application;

import com.jrlcst.couponapi.shared.infraestructure.rest.dto.DefaultResponseDto;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.UUID;

public final class DefaultResponseFactory {

    private DefaultResponseFactory() {
        throw new IllegalStateException("Utility class");
    }

    private static final String REQUEST_ID = "requestId";

    public static <T> DefaultResponseDto<T> success(T data, String message) {
        return DefaultResponseDto.<T>builder()
                .requestId(getRequestId())
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> DefaultResponseDto<T> success(String message) {
        return DefaultResponseDto.<T>builder()
                .requestId(getRequestId())
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> DefaultResponseDto<T> notFound(String message) {
        return DefaultResponseDto.<T>builder()
                .requestId(getRequestId())
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> DefaultResponseDto<T> error(String message) {
        return DefaultResponseDto.<T>builder()
                .requestId(getRequestId())
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    private static UUID getRequestId() {
        String value = MDC.get(REQUEST_ID);
        return value != null ? UUID.fromString(value) : null;
    }
}
