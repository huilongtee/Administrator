package com.example.Administrator.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    public static final String CODE_SUCCESS = "200";
    public static final String CODE_SYS_ERROR = "500";
    public static final String CODE_AUTH_ERROR = "400";
//    Code 401=authorization error, we need to deal with it ourself

    private String code;
    private String message;
    private Object data;

    public static Result success() {
//        return new Result(CODE_SUCCESS, "success", null);
    return Result.builder().code(CODE_SUCCESS).message("success").build();
    }

    public static Result success(Object data) {
        return new Result(CODE_SUCCESS, "success", data);
    }

    public static Result error() {
        return new Result(CODE_SYS_ERROR, "System error", null);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result error(String msg) {
        return new Result(CODE_SYS_ERROR, msg, null);
    }
}
