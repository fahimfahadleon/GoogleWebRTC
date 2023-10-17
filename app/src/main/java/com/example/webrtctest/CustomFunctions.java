package com.example.webrtctest;

import kotlin.Unit;

public class CustomFunctions {
    public static <T> Result<T> createValue(Operation<T> operation) {
        try {
            T result = operation.perform();
            return Result.success(result);
        } catch (Exception e) {
            return Result.failure(e);
        }
    }

    public static Result<Unit> setValue(Action operation) {
        try {
            operation.perform();
            return Result.success(Unit.INSTANCE);
        } catch (Exception e) {
            return Result.failure(e);
        }
    }
}

interface Operation<T> {
    T perform() throws Exception;
}

interface Action {
    void perform() throws Exception;
}