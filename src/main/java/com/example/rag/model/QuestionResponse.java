package com.example.rag.model;

import java.util.List;

/**
 * Response DTO for the /ask endpoint.
 * Contains the answer, retrieved documents, and processing metadata.
 */
public class QuestionResponse {
    private String question;
    private String answer;
    private List<RetrievedDocument> retrievedDocuments;
    private long processingTimeMs;

    public QuestionResponse() {
    }

    public QuestionResponse(String question, String answer,
            List<RetrievedDocument> retrievedDocuments,
            long processingTimeMs) {
        this.question = question;
        this.answer = answer;
        this.retrievedDocuments = retrievedDocuments;
        this.processingTimeMs = processingTimeMs;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<RetrievedDocument> getRetrievedDocuments() {
        return retrievedDocuments;
    }

    public void setRetrievedDocuments(List<RetrievedDocument> retrievedDocuments) {
        this.retrievedDocuments = retrievedDocuments;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    @Override
    public String toString() {
        return "QuestionResponse{" +
                "question='" + question + '\'' +
                ", answer='" + answer.substring(0, Math.min(50, answer.length())) + "...'" +
                ", retrievedDocumentsCount=" + (retrievedDocuments != null ? retrievedDocuments.size() : 0) +
                ", processingTimeMs=" + processingTimeMs +
                '}';
    }
}
