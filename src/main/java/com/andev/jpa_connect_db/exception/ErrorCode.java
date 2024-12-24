package com.andev.jpa_connect_db.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    USER_EXISTED(400,"User is existed!!!", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999,"Ngoại lệ không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXIST(1005, "Không tồn tại User có Username: %s", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1007, "Truy cập bị chặn", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED);
    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode=httpStatusCode;
    }

    public String stringFormat(Object... objects){
        return String.format(message, objects);
    }
}
