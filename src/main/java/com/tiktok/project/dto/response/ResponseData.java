package com.tiktok.project.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class ResponseData<T>{
    private final int status;
    private final String message;
    private T data;

    // PUT, PATCH, DELETE
    public ResponseData(HttpStatusCode status, String message) {
        this.status = status.value();
        this.message = message;
    }
    // GET, POST
    public ResponseData(HttpStatusCode status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }
}
