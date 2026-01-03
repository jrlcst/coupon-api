package com.jrlcst.couponapi.shared.infraestructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class DefaultResponseDto<T> {

    @Schema(description = "The Request ID is a unique identifier assigned to each request")
    private final UUID requestId;

    @Schema(description = "Message describing the response")
    private final String message;

    @Schema(description = "Data returned in the response")
    private final T data;

    @Schema(description = "Timestamp of the request")
    private final Instant timestamp;

    private DefaultResponseDto(Builder<T> builder) {
        this.requestId = builder.requestId;
        this.message = builder.message;
        this.data = builder.data;
        this.timestamp = builder.timestamp;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private UUID requestId;
        private String message;
        private T data;
        private Instant timestamp;

        public Builder<T> requestId(UUID requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public DefaultResponseDto<T> build() {
            return new DefaultResponseDto<>(this);
        }
    }
}
