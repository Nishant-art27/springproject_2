package com.example.e_commerce1.exception;

import com.example.e_commerce1.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for the entire application.
 * 
 * This catches exceptions thrown anywhere in the app and converts them
 * into user-friendly API responses. Instead of ugly stack traces,
 * users get helpful error messages explaining what went wrong.
 * 
 * Think of this as your app's "crisis management team" - when things go wrong,
 * they handle it gracefully and communicate clearly with the user.
 * 
 * @author Your Name
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation errors from @Valid annotations.
     * 
     * When a user sends invalid data (e.g., negative price, empty name),
     * this catches it and returns all validation errors in a friendly format.
     * 
     * Example response:
     * {
     *   "success": false,
     *   "message": "Validation failed. Please check your input.",
     *   "data": {
     *     "price": "Price must be at least 0.01",
     *     "name": "Product name is required"
     *   }
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        logger.warn("Validation error occurred: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        
        // Collect all validation errors
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponse<Map<String, String>> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Validation failed. Please check your input and try again.");
        response.setData(errors);
        response.setErrorCode("VALIDATION_ERROR");
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handles ProductNotFoundException.
     * 
     * When a user tries to access a product that doesn't exist,
     * this gives them a clear 404 response.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFoundException(
            ProductNotFoundException ex) {
        
        logger.warn("Product not found: {}", ex.getMessage());
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), "PRODUCT_NOT_FOUND"));
    }

    /**
     * Handles OrderNotFoundException.
     * 
     * Similar to product not found, but for orders.
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleOrderNotFoundException(
            OrderNotFoundException ex) {
        
        logger.warn("Order not found: {}", ex.getMessage());
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), "ORDER_NOT_FOUND"));
    }

    /**
     * Handles InsufficientStockException.
     * 
     * When a user tries to buy more items than we have in stock,
     * this explains the situation clearly.
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsufficientStockException(
            InsufficientStockException ex) {
        
        logger.warn("Insufficient stock: {}", ex.getMessage());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage() + " Please reduce the quantity or remove the item.",
                        "INSUFFICIENT_STOCK"));
    }

    /**
     * Handles EmptyCartException.
     * 
     * When a user tries to place an order with an empty cart.
     */
    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmptyCartException(
            EmptyCartException ex) {
        
        logger.warn("Empty cart exception: {}", ex.getMessage());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Your cart is empty. Add some items before placing an order!",
                        "EMPTY_CART"));
    }

    /**
     * Handles IllegalArgumentException.
     * 
     * General purpose handler for invalid method arguments.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        
        logger.warn("Illegal argument: {}", ex.getMessage());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Invalid request: " + ex.getMessage(),
                        "INVALID_ARGUMENT"));
    }

    /**
     * Handles type mismatch errors.
     * 
     * For example, when someone sends "abc" for a price field that expects a number.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        
        logger.warn("Type mismatch error: {}", ex.getMessage());
        
        String message = String.format(
                "Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(),
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, "TYPE_MISMATCH"));
    }

    /**
     * Catches all other unexpected exceptions.
     * 
     * This is our safety net - if something goes wrong that we didn't anticipate,
     * we still return a graceful error instead of crashing.
     * 
     * In production, you'd want to:
     * - Log full stack trace to file
     * - Send alert to monitoring system
     * - Return generic message to user (don't expose internals)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        
        // In production, don't expose internal error details
        String userMessage = "An unexpected error occurred. Our team has been notified. " +
                           "Please try again later or contact support if the problem persists.";
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(userMessage, "INTERNAL_ERROR"));
    }
}
