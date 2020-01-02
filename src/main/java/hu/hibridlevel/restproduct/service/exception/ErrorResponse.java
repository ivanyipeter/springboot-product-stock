package hu.hibridlevel.restproduct.service.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String error;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponse(LocalDateTime time,int status, String error) {
        this.error = error;
        this.status = status;
        this.timestamp = time;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
