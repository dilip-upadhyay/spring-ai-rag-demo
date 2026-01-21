package com.example.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Spring AI RAG Demo.
 * 
 * This application demonstrates Retrieval-Augmented Generation (RAG) using:
 * - Spring Boot 3.4.2
 * - Spring AI 1.0.0-M5
 * - OpenAI for embeddings and chat
 * - In-memory vector store
 * 
 * @author Dilip Upadhyay (dilip_upadhyay@outlook.com)
 */
@SpringBootApplication
public class RagApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagApplication.class, args);
    }
}
