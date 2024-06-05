package com.nvd.footballmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nvd.footballmanager.utils.Constants;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {
    @JsonProperty("message")
    private String message = Constants.SUCCESS;

    @JsonProperty("code")
    private Integer code = HttpStatus.OK.value();

    @JsonProperty("status")
    private HttpStatus status = HttpStatus.OK;

    @JsonProperty("success")
    @Builder.Default
    private Boolean success = true;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("timestamp")
    @Builder.Default
    private Instant timestamp = Instant.now();

    public static ApiResponse success(Object o) {
        return ApiResponse.builder()
                .message(Constants.SUCCESS)
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(o)
                .build();
    }

    public static ApiResponse badRequest(String message) {
        return ApiResponse.builder()
                .message(message)
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
    }

    public static ApiResponse notFound(String message) {
        return ApiResponse.builder()
                .message(message)
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .success(false)
                .build();
    }

    public static ApiResponse noContent() {
        return ApiResponse.builder()
                .message(Constants.OBJECT_DELETED)
                .code(HttpStatus.NO_CONTENT.value())
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    public static ApiResponse notFound() {
        return ApiResponse.builder()
                .message(Constants.DATA_NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .success(false)
                .build();
    }

    public static ApiResponse created(Object o) {
        return ApiResponse.builder()
                .message(Constants.CREATED)
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(o)
                .build();
    }


    public static ApiResponse forbidden() {
        return ApiResponse.builder()
                .message("Forbidden")
                .code(HttpStatus.FORBIDDEN.value())
                .status(HttpStatus.FORBIDDEN)
                .success(false)
                .build();
    }

    public static ApiResponse unauthorized() {
        return ApiResponse.builder()
                .message("UnAuthorized")
                .code(HttpStatus.UNAUTHORIZED.value())
                .status(HttpStatus.UNAUTHORIZED)
                .success(false)
                .build();
    }
}
