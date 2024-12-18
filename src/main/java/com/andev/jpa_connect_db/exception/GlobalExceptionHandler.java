package com.andev.jpa_connect_db.exception;

import com.andev.jpa_connect_db.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> globalExceptionHandler(Exception e){
        ApiResponse<String> apiResponse= new ApiResponse<>();
        apiResponse.setMessage(e.getMessage());
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> globalMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(9999);
        apiResponse.setMessage(exception.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> globalUserNotFoundException(AppException exception){
        ApiResponse<String> apiResponse= new ApiResponse<>();
        apiResponse.setMessage(exception.getMessage());
        apiResponse.setCode(exception.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
