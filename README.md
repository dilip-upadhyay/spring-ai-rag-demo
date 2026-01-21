# Spring AI RAG Demo Application

A minimal Spring Boot application demonstrating **Retrieval-Augmented Generation (RAG)** using Spring AI.

## 🚀 Features

- **REST API**: POST `/ask` endpoint for question answering
- **Spring AI Integration**: ChatClient and EmbeddingClient
- **Vector Store**: In-memory vector store with cosine similarity search
- **RAG Pipeline**: Complete flow from query embedding to answer generation
- **Structured Responses**: Includes retrieved documents and similarity scores
- **Error Handling**: Comprehensive exception handling and validation

## 🛠️ Technology Stack

- **Java**: 21
- **Spring Boot**: 3.4.2
- **Spring AI**: 1.0.0-M5
- **OpenAI**: GPT-3.5-turbo (chat) + text-embedding-ada-002 (embeddings)
- **Build Tool**: Maven

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.9+
- OpenAI API key

## ⚙️ Setup

### 1. Clone/Download the project

```bash
cd spring-ai-rag
```

### 2. Set your OpenAI API key

**Option A: Environment Variable (Recommended)**
```bash
export OPENAI_API_KEY=sk-your-api-key-here
```

**Option B: Update application.yml**
```yaml
spring:
  ai:
    openai:
      api-key: sk-your-api-key-here
```

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🔍 Usage

### Health Check

```bash
curl http://localhost:8080/
```

**Response:**
```
Spring AI RAG application is running!
```

### Ask a Question

```bash
curl -X POST http://localhost:8080/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is Spring AI?"}'
```

**Response:**
```json
{
  "question": "What is Spring AI?",
  "answer": "Spring AI is a framework for building AI-powered applications using Spring Boot...",
  "retrievedDocuments": [
    {
      "documentId": "doc-1",
      "content": "[Document 1]\nSpring AI is a framework...",
      "similarity": 0.8923
    }
  ],
  "processingTimeMs": 1234
}
```

### Sample Questions

```bash
# Question 1: Spring AI
curl -X POST http://localhost:8080/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is Spring AI?"}'

# Question 2: RAG
curl -X POST http://localhost:8080/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Explain Retrieval-Augmented Generation"}'

# Question 3: Vector Embeddings
curl -X POST http://localhost:8080/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What are vector embeddings?"}'

# Question 4: Out-of-context (should handle gracefully)
curl -X POST http://localhost:8080/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "How does quantum computing work?"}'
```

## 📚 How It Works

### RAG Pipeline

```
User Question
     ↓
1. Query Embedding (EmbeddingModel)
     ↓
2. Similarity Search (Vector Store)
     ↓
3. Retrieve Top-K Documents
     ↓
4. Build Enriched Prompt (PromptTemplate)
     ↓
5. Generate Answer (ChatModel)
     ↓
Response with Answer & Metadata
```

### Sample Documents

The application uses 3 hardcoded sample documents:

1. **doc-1**: Spring AI framework description
2. **doc-2**: Retrieval-Augmented Generation explanation
3. **doc-3**: Vector embeddings overview

## 📁 Project Structure

```
src/main/java/com/example/rag/
├── RagApplication.java           # Main entry point
├── controller/
│   └── AskController.java        # REST endpoint
├── service/
│   ├── RagService.java          # RAG orchestration
│   └── DocumentService.java     # Document management
├── store/
│   └── InMemoryVectorStore.java # Vector similarity search
├── model/
│   ├── Document.java            # Domain model
│   ├── QuestionRequest.java     # Request DTO
│   ├── QuestionResponse.java    # Response DTO
│   └── RetrievedDocument.java   # Metadata DTO
├── config/
│   └── SpringAiConfig.java      # Spring AI configuration
└── exception/
    ├── GlobalExceptionHandler.java
    ├── InvalidQuestionException.java
    └── RagException.java
```

## 🔧 Configuration

Edit `src/main/resources/application.yml`:

```yaml
rag:
  vector-store:
    similarity-threshold: 0.7  # Minimum similarity for retrieval (0.0-1.0)
    max-results: 2             # Number of documents to retrieve

spring:
  ai:
    openai:
      chat:
        options:
          temperature: 0.3     # Lower = more factual, Higher = more creative
          max-tokens: 500      # Maximum response length
```

## 🧪 Testing

Run tests:
```bash
mvn test
```

## 📖 API Documentation

### POST /ask

**Request:**
```json
{
  "question": "Your question here"
}
```

**Response:**
```json
{
  "question": "Your question",
  "answer": "Generated answer based on context",
  "retrievedDocuments": [
    {
      "documentId": "doc-id",
      "content": "Retrieved document content",
      "similarity": 0.85
    }
  ],
  "processingTimeMs": 1234
}
```

**Error Response:**
```json
{
  "timestamp": "2026-01-22T00:30:09",
  "status": 400,
  "error": "Bad Request",
  "message": "Question cannot be empty",
  "path": "/ask"
}
```

## 🎯 Key Features Implemented

### Must-Have ✅
- [x] POST /ask endpoint
- [x] Spring Boot application
- [x] Spring AI ChatClient integration
- [x] Spring AI EmbeddingClient integration
- [x] In-memory vector store
- [x] Hardcoded sample documents
- [x] Document embedding
- [x] Similarity search
- [x] Prompt construction with context

### Nice-to-Have ✅
- [x] Clear separation of concerns (Controller/Service/Store layers)
- [x] Comprehensive error handling
- [x] Structured prompt template
- [x] Request validation
- [x] Response metadata (similarity scores, timing)
- [x] Logging and debugging

## 🚀 Future Enhancements

- [ ] Persistent vector store (PostgreSQL with pgvector, Qdrant, Pinecone)
- [ ] File upload for dynamic document ingestion
- [ ] Streaming responses
- [ ] Conversation history support
- [ ] Advanced chunking strategies
- [ ] Hybrid search (keyword + semantic)
- [ ] Citation tracking

## 🐛 Troubleshooting

### Issue: Application fails to start

**Solution:** Ensure you have set the OPENAI_API_KEY environment variable or updated application.yml

### Issue: "Failed to initialize document embeddings"

**Solution:** Check your OpenAI API key and internet connection. Verify your API key has sufficient credits.

### Issue: Low similarity scores for relevant questions

**Solution:** Adjust the `similarity-threshold` in application.yml or improve document content.

## 📝 License

This is a demo application for demonstration purposes.

## 👤 Author

**Dilip Upadhyay**  
Email: dilip_upadhyay@outlook.com  
GitHub: [github.com/dilip-upadhyay](https://github.com/dilip-upadhyay)  
Project: Spring AI RAG Demo - Built with Java 21 and Spring Boot 3.4.2

---

**Happy RAG-ing! 🤖**
