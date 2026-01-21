package com.example.rag.model;

import java.util.List;

/**
 * Domain model representing a document in the RAG system.
 * Contains document content and its vector embedding.
 */
public class Document {
    private String id;
    private String content;
    private List<Double> embedding;

    public Document() {
    }

    public Document(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public Document(String id, String content, List<Double> embedding) {
        this.id = id;
        this.content = content;
        this.embedding = embedding;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", content='" + content.substring(0, Math.min(50, content.length())) + "...'" +
                ", embeddingSize=" + (embedding != null ? embedding.size() : 0) +
                '}';
    }
}
