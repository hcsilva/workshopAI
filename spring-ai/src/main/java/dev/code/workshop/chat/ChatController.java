package dev.code.workshop.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final OllamaChatModel model;
    private final ChatClient chatClient;


    public ChatController(OllamaChatModel model, ChatClient.Builder builder) {
        this.model = model;
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chat(){
        return model.call("Qual a capital do Brasil");
    }

    @GetMapping("/chatClient")
    public String chatClient(){
        return chatClient.prompt()
                .user("Tell me an interesting fact about Java")
                .call()
                .content();
    }

    @GetMapping("/stream")
    public Flux<String> stream() {
        return chatClient.prompt()
                .user(u -> u.text("I'm visiting Hilton Head soon, can you give me 10 places I must visit?"))
                .stream()
                .content();
    }

    @GetMapping("/chatResponse")
    public ChatResponse chatClient2(){
        return chatClient.prompt()
                .user("Tell me a joke")
                .call()
                .chatResponse();
    }
}
