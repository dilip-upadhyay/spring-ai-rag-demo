package com.example.rag.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuration class for Spring AI components.
 * The ChatModel and EmbeddingModel beans are auto-configured by
 * spring-ai-openai-spring-boot-starter based on application.yml settings.
 */
@Configuration
public class SpringAiConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringAiConfig.class);

    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;

    public SpringAiConfig(ChatModel chatModel, EmbeddingModel embeddingModel) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
    }

    @PostConstruct
    public void init() {
        logger.info("Spring AI configuration initialized");
        logger.info("ChatModel: {}", chatModel.getClass().getSimpleName());
        logger.info("EmbeddingModel: {}", embeddingModel.getClass().getSimpleName());
    }
}
