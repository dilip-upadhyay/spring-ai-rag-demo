package com.example.rag.store;

import com.example.rag.model.Document;
import com.example.rag.model.RetrievedDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory vector store for document embeddings.
 * Uses cosine similarity for semantic search.
 */
@Component
public class InMemoryVectorStore {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryVectorStore.class);

    private final Map<String, Document> documents = new ConcurrentHashMap<>();

    /**
     * Add a document with its embedding to the store.
     */
    public void addDocument(Document document) {
        if (document.getId() == null || document.getEmbedding() == null) {
            throw new IllegalArgumentException("Document must have id and embedding");
        }
        documents.put(document.getId(), document);
        logger.debug("Added document {} to vector store", document.getId());
    }

    /**
     * Perform similarity search to find top-K most similar documents.
     * 
     * @param queryEmbedding the query vector
     * @param topK           number of results to return
     * @param threshold      minimum similarity threshold (0.0 to 1.0)
     * @return list of retrieved documents with similarity scores
     */
    public List<RetrievedDocument> similaritySearch(List<Double> queryEmbedding, int topK, double threshold) {
        logger.debug("Performing similarity search with topK={}, threshold={}", topK, threshold);

        if (queryEmbedding == null || queryEmbedding.isEmpty()) {
            throw new IllegalArgumentException("Query embedding cannot be null or empty");
        }

        return documents.values().stream()
                .map(doc -> {
                    double similarity = cosineSimilarity(queryEmbedding, doc.getEmbedding());
                    return new RetrievedDocument(doc.getId(), doc.getContent(), similarity);
                })
                .filter(retrieved -> retrieved.getSimilarity() >= threshold)
                .sorted(Comparator.comparingDouble(RetrievedDocument::getSimilarity).reversed())
                .limit(topK)
                .collect(Collectors.toList());
    }

    /**
     * Calculate cosine similarity between two vectors.
     * Result ranges from -1 to 1, where 1 means identical vectors.
     */
    private double cosineSimilarity(List<Double> vec1, List<Double> vec2) {
        if (vec1.size() != vec2.size()) {
            throw new IllegalArgumentException("Vectors must have same dimensions");
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vec1.size(); i++) {
            dotProduct += vec1.get(i) * vec2.get(i);
            norm1 += vec1.get(i) * vec1.get(i);
            norm2 += vec2.get(i) * vec2.get(i);
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * Get total number of documents in the store.
     */
    public int size() {
        return documents.size();
    }

    /**
     * Clear all documents from the store.
     */
    public void clear() {
        documents.clear();
        logger.info("Cleared all documents from vector store");
    }

    /**
     * Get a document by ID.
     */
    public Optional<Document> getDocument(String id) {
        return Optional.ofNullable(documents.get(id));
    }
}
