package com.tiktok.project.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL) // Loại bỏ data nếu null
public class ResponseError extends ResponseData<Object>{
    public ResponseError(HttpStatusCode status, String message) {
        super(status, message);
    }
}
