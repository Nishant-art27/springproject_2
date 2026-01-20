package com.example.e_commerce1.dto;

import java.time.Instant;

/**
 * Generic API response wrapper for consistent response structure.
 * 
 * This makes our API predictable and easy to consume by frontend applications.
 * Every response follows the same pattern, making error handling easier for clients.
 * 
 * @param <T> The type of data being returned
 */
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private Instant timestamp;
    private String errorCode;  // For programmatic error handling
    
    /**
     * Creates a successful response with data.
     * Use this when the operation completes successfully.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(Instant.now());
        return response;
    }
    
    /**
     * Creates a successful response with just a message (no data).
     * Useful for DELETE operations or confirmations.
     */
    public static <T> ApiResponse<T> success(String message) {
        return success(null, message);
    }
    
    /**
     * Creates an error response.
     * Use this when something goes wrong but we handle it gracefully.
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrorCode(errorCode);
        response.setTimestamp(Instant.now());
        return response;
    }
    
    /**
     * Creates an error response with just a message.
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(message, "GENERAL_ERROR");
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
