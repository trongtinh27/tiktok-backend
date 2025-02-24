package com.tiktok.project.controller;


import com.tiktok.project.dto.response.ResponseData;
import com.tiktok.project.entity.User;
import com.tiktok.project.service.ViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/views")
@Validated
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ViewController {
    private final ViewService viewService;

    @PostMapping("/{videoId}")
    public ResponseData<?> addView(Authentication authentication, @PathVariable int videoId) {
        viewService.saveView(authentication, videoId);
        return new ResponseData<>(HttpStatus.OK, "Add view for video: " + videoId + " successfully");
    }
}

