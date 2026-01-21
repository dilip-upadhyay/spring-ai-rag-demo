package com.example.rag.service;

import com.example.rag.model.QuestionResponse;
import com.example.rag.model.RetrievedDocument;
import com.example.rag.store.InMemoryVectorStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service that orchestrates the RAG (Retrieval-Augmented Generation) workflow.
 */
@Service
public class RagService {

    private static final Logger logger = LoggerFactory.getLogger(RagService.class);

    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final InMemoryVectorStore vectorStore;

    @Value("${rag.vector-store.similarity-threshold:0.7}")
    private double similarityThreshold;

    @Value("${rag.vector-store.max-results:2}")
    private int maxResults;

    @Value("classpath:prompts/rag-template.txt")
    private Resource promptTemplate;

    public RagService(ChatModel chatModel,
            EmbeddingModel embeddingModel,
            InMemoryVectorStore vectorStore) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    /**
     * Process a question through the complete RAG pipeline.
     * 
     * @param question the user's question
     * @return response containing answer and metadata
     */
    public QuestionResponse askQuestion(String question) {
        long startTime = System.currentTimeMillis();

        logger.info("Processing question: {}", question);

        try {
            // Step 1: Embed the question
            logger.debug("Step 1: Generating query embedding");
            List<Double> queryEmbedding = embedQuestion(question);

            // Step 2: Retrieve relevant documents
            logger.debug("Step 2: Performing similarity search");
            List<RetrievedDocument> retrievedDocs = vectorStore.similaritySearch(
                    queryEmbedding,
                    maxResults,
                    similarityThreshold);

            logger.info("Retrieved {} documents with similarities: {}",
                    retrievedDocs.size(),
                    retrievedDocs.stream()
                            .map(d -> String.format("%.3f", d.getSimilarity()))
                            .toList());

            // Step 3: Build enriched prompt with context
            logger.debug("Step 3: Building prompt with retrieved context");
            String enrichedPrompt = buildPromptWithContext(question, retrievedDocs);

            // Step 4: Generate answer using LLM
            logger.debug("Step 4: Generating answer with LLM");
            String answer = generateAnswer(enrichedPrompt);

            long processingTime = System.currentTimeMillis() - startTime;
            logger.info("Question processed in {}ms", processingTime);

            return new QuestionResponse(question, answer, retrievedDocs, processingTime);

        } catch (Exception e) {
            logger.error("Error processing question: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process question: " + e.getMessage(), e);
        }
    }

    /**
     * Generate embedding for the user's question.
     */
    private List<Double> embedQuestion(String question) {
        try {
            EmbeddingResponse response = embeddingModel.embedForResponse(List.of(question));
            float[] rawEmbedding = response.getResults().get(0).getOutput();

            // Convert float[] to List<Double>
            List<Double> embedding = new ArrayList<>(rawEmbedding.length);
            for (float value : rawEmbedding) {
                embedding.add((double) value);
            }
            return embedding;
        } catch (Exception e) {
            logger.error("Failed to generate question embedding: {}", e.getMessage());
            throw new RuntimeException("Embedding generation failed", e);
        }
    }

    /**
     * Build an enriched prompt by injecting retrieved documents as context.
     */
    private String buildPromptWithContext(String question, List<RetrievedDocument> documents) {
        try {
            // Format context with numbered documents
            StringBuilder contextBuilder = new StringBuilder();
            for (int i = 0; i < documents.size(); i++) {
                contextBuilder.append(String.format("[Document %d]%n", i + 1));
                contextBuilder.append(documents.get(i).getContent());
                if (i < documents.size() - 1) {
                    contextBuilder.append("\n\n");
                }
            }

            String context = contextBuilder.toString();

            // Load and populate template
            String templateContent = promptTemplate.getContentAsString(StandardCharsets.UTF_8);
            PromptTemplate template = new PromptTemplate(templateContent);

            Prompt prompt = template.create(Map.of(
                    "context", context,
                    "question", question));

            String fullPrompt = prompt.getContents();
            logger.debug("Generated prompt with {} characters of context", context.length());

            return fullPrompt;

        } catch (IOException e) {
            logger.error("Failed to load prompt template: {}", e.getMessage());
            throw new RuntimeException("Prompt construction failed", e);
        }
    }

    /**
     * Generate answer using the chat model.
     */
    private String generateAnswer(String prompt) {
        try {
            String answer = chatModel.call(prompt);
            logger.debug("Generated answer: {} characters", answer.length());
            return answer;
        } catch (Exception e) {
            logger.error("Failed to generate answer: {}", e.getMessage());
            throw new RuntimeException("Answer generation failed", e);
        }
    }
}
