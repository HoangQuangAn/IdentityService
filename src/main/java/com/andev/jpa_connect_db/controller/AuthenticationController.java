package com.andev.jpa_connect_db.controller;

import com.andev.jpa_connect_db.dto.request.ApiResponse;
import com.andev.jpa_connect_db.dto.request.AuthenticationRequest;
import com.andev.jpa_connect_db.dto.request.IntrospectRequest;
import com.andev.jpa_connect_db.dto.response.AuthenticationResponse;
import com.andev.jpa_connect_db.dto.response.IntrospectResponse;
import com.andev.jpa_connect_db.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> logIn(@RequestBody AuthenticationRequest request) throws JOSEException {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(authenticationResponse.isAuthenticated() ? HttpStatus.OK.value() : HttpStatus.UNAUTHORIZED.value())
                .result(authenticationResponse)
                .message(authenticationResponse.isAuthenticated() ? "Login successful" : "Invalid username or password").build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> validateToken(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspectResponse(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
