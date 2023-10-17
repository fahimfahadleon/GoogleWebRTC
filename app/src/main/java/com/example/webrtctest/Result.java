package com.example.webrtctest;

public class Result<T> {
    private final boolean success;
    private final T data;
    private final Exception error;

    private Result(boolean success, T data, Exception error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null);
    }

    public static <T> Result<T> failure(Exception error) {
        return new Result<>(false, null, error);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public Exception getError() {
        return error;
    }
}