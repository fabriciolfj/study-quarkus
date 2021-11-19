package com.github.fabriciolfj.account.adapter.in.dto;


public class ErrorResponse {

    private String exceptionType;
    private int code;
    private String error;

    public ErrorResponse() {

    }

    public ErrorResponse(String exceptionType, int code, String error) {
        this.exceptionType = exceptionType;
        this.code = code;
        this.error = error;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
