package com.andev.jpa_connect_db.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException{
    private int code;
    public AppException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code= errorCode.getCode();
    }

    public AppException(ErrorCode errorCode, Object... objects){
        super(String.format(errorCode.getMessage(), objects));
        this.code=errorCode.getCode();
    }

}
