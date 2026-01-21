package com.example.rag.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for the /ask endpoint.
 */
public class QuestionRequest {
    
    @NotBlank(message = "Question cannot be empty")
    private String question;

    public QuestionRequest() {
    }

    public QuestionRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "QuestionRequest{" +
                "question='" + question + '\'' +
                '}';
    }
}
