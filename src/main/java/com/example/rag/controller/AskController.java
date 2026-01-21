package com.example.rag.controller;

import com.example.rag.model.QuestionRequest;
import com.example.rag.model.QuestionResponse;
import com.example.rag.service.RagService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for the RAG question-answering endpoint.
 */
@RestController
@RequestMapping
public class AskController {

    private static final Logger logger = LoggerFactory.getLogger(AskController.class);

    private final RagService ragService;

    public AskController(RagService ragService) {
        this.ragService = ragService;
    }

    /**
     * POST /ask - Answer a question using RAG.
     * 
     * @param request the question request
     * @return response containing answer and retrieved documents
     */
    @PostMapping("/ask")
    public ResponseEntity<QuestionResponse> ask(@Valid @RequestBody QuestionRequest request) {
        logger.info("Received question: {}", request.getQuestion());

        QuestionResponse response = ragService.askQuestion(request.getQuestion());

        logger.info("Returning answer with {} retrieved documents, processed in {}ms",
                response.getRetrievedDocuments().size(),
                response.getProcessingTimeMs());

        return ResponseEntity.ok(response);
    }

    /**
     * GET / - Health check endpoint.
     */
    @GetMapping("/")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Spring AI RAG application is running!");
    }
}
