package com.springai.rag_pgvector_openai_chatbot;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OpenAIChatService {
    private final PgVectorStore vectorStore;
    private final ChatClient chatClient;

    public OpenAIChatService(PgVectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    public String chat(String message, Resource template) {
        List<Document> documents = this.vectorStore.similaritySearch(message);
        log.info("Found {} documents", documents != null ? documents.size() : 0);

        String collect = documents != null ? documents.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator())) : "";
        Message createdMessage = new SystemPromptTemplate(template).createMessage(Map.of("documents", collect));
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(createdMessage, userMessage));
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        
        if (chatResponse == null) {
            return "";
        }
        
        return chatResponse.getResults().stream().map(generation -> {
            return generation.getOutput().getContent();
        }).collect(Collectors.joining("/n"));
    }
}
