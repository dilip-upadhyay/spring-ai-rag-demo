package com.example.rag.model;

/**
 * Represents a retrieved document with similarity score.
 */
public class RetrievedDocument {
    private String documentId;
    private String content;
    private double similarity;

    public RetrievedDocument() {
    }

    public RetrievedDocument(String documentId, String content, double similarity) {
        this.documentId = documentId;
        this.content = content;
        this.similarity = similarity;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        return "RetrievedDocument{" +
                "documentId='" + documentId + '\'' +
                ", similarity=" + String.format("%.4f", similarity) +
                '}';
    }
}
