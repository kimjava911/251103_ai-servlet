package org.example.aiservlet.service;

import com.google.genai.Client;
import io.github.cdimascio.dotenv.Dotenv;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

// https://aistudio.google.com/
// https://aistudio.google.com/api-keys
// https://groq.com/
// https://console.groq.com/keys
public class AIService {
    private final Client geminiClient;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String groqKey;

    public AIService() {
        // 배포 시에 .env가 github에 포힘되어 있지 않음 -> 환경변수
//        Dotenv dotenv = Dotenv.load(); // resources 안에 dotenv가 없으면 에러가 남
        // .env 없이 환경변수로 호환해서 쓰려면
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // 이게 있어야 dotenv가 없을 경우 환경변수를 읽어서 대신 사용
                .load();
//        String apiKey = dotenv.get("GOOGLE_API_KEY");
        geminiClient = Client.builder().apiKey(
                dotenv.get("GOOGLE_API_KEY")
        ).build();
        groqKey = dotenv.get("GROQ_API_KEY");
    }

    public String chat(String text) {
        return geminiClient.models.generateContent(
                "gemini-2.5-flash", text, null).text();
    }

    // https://console.groq.com/docs/quickstart
    /*
    curl -X POST "https://api.groq.com/openai/v1/chat/completions" \
     -H "Authorization: Bearer $GROQ_API_KEY" \
     -H "Content-Type: application/json" \
     -d '{"messages": [{"role": "user", "content": "Explain the importance of fast language models"}],
        "model": "llama-3.3-70b-versatile"}'
     */
    public String chatByGroq(String text) {
        try {
            // https://console.groq.com/docs/models
            HttpResponse<String> response = httpClient.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                            .header("Authorization", "Bearer %s".formatted(groqKey))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(
                                    objectMapper.writeValueAsString(new GroqRequest(
                                            List.of(new GroqRequest.Message("user", text)), "openai/gpt-oss-120b")
                                    ))
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );
//            return response.body();
            return objectMapper.readValue(response.body(), GroqResponse.class)
                    .choices.get(0).message.content;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    record GroqRequest(List<Message> messages, String model) {
        record Message(String role, String content) {}
    }

    record GroqResponse(List<Choice> choices) {
        record Choice(Message message) {
            record Message(String content) {}
        }
    }
}
