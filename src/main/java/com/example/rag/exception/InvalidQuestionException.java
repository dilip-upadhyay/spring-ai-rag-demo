package com.example.rag.exception;

/**
 * Custom exception for invalid questions.
 */
public class InvalidQuestionException extends RuntimeException {

    public InvalidQuestionException(String message) {
        super(message);
    }

    public InvalidQuestionException(String message, Throwable cause) {
        super(message, cause);
    }
}
