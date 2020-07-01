package com.exercise.MeetingScheduler.excpertions.handlers;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiError {
    private HttpStatus status;
    private String errMessage;
    List<String> errors;

    public ApiError(HttpStatus status, String errMessage, List<String> errors) {
        this.status = status;
        this.errMessage = errMessage;
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "status=" + status +
                ", errMessage='" + errMessage + '\'' +
                ", errors=" + errors +
                '}';
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
