package com.andev.jpa_connect_db.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException{
    private ErrorCode errorCode;
    public AppException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }

    public AppException(ErrorCode errorCode, Object... objects){
        super(String.format(errorCode.getMessage(), objects));
        this.errorCode=errorCode;
    }

}
