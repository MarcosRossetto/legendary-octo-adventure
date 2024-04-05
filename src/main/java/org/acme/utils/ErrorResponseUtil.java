package org.acme.utils;

public class ErrorResponseUtil {
    public String uuid;
    public String message;
    public String timestamp;
    public int status;

    public ErrorResponseUtil(String uuid, String message, String timestamp, int status) {
        this.uuid = uuid;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }
}
