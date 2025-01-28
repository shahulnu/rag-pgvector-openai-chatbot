package com.springai.rag_pgvector_openai_chatbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final OpenAIChatService openAIChatService;

    @Value("classpath:/assist.st")
    private Resource assist;

    public ChatController(OpenAIChatService openAIChatService) {
        this.openAIChatService = openAIChatService;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "message") String message) {
        return openAIChatService.chat(message, assist);
    }
}
