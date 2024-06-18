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
public class CustomApiResponse {
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

    public static CustomApiResponse success(Object o) {
        return CustomApiResponse.builder()
                .message(Constants.SUCCESS)
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(o)
                .build();
    }

    public static CustomApiResponse badRequest(String message) {
        return CustomApiResponse.builder()
                .message(message)
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
    }

    public static CustomApiResponse notFound(String message) {
        return CustomApiResponse.builder()
                .message(message)
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .success(false)
                .build();
    }

    public static CustomApiResponse noContent() {
        return CustomApiResponse.builder()
                .message(Constants.OBJECT_DELETED)
                .code(HttpStatus.NO_CONTENT.value())
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    public static CustomApiResponse notFound() {
        return CustomApiResponse.builder()
                .message(Constants.DATA_NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .success(false)
                .build();
    }

    public static CustomApiResponse created(Object o) {
        return CustomApiResponse.builder()
                .message(Constants.CREATED)
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(o)
                .build();
    }


    public static CustomApiResponse forbidden() {
        return CustomApiResponse.builder()
                .message("Forbidden")
                .code(HttpStatus.FORBIDDEN.value())
                .status(HttpStatus.FORBIDDEN)
                .success(false)
                .build();
    }

    public static CustomApiResponse unauthorized() {
        return CustomApiResponse.builder()
                .message("UnAuthorized")
                .code(HttpStatus.UNAUTHORIZED.value())
                .status(HttpStatus.UNAUTHORIZED)
                .success(false)
                .build();
    }
}
