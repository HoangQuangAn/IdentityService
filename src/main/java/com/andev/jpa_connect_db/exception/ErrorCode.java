package com.andev.jpa_connect_db.exception;


import lombok.Getter;

@Getter
public enum ErrorCode {

    USER_EXISTED(400,"User is existed!!!"),
    UNCATEGORIZED_EXCEPTION(9999,"Ngoại lệ không xác định"),
    USER_NOT_EXIST(1005, "Không tồn tại User có Username: %s"),
    UNAUTHENTICATED(1006, "Unauthenticated");
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String stringFormat(Object... objects){
        return String.format(message, objects);
    }
}
