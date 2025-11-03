package org.example.aiservlet.service;

import com.google.genai.Client;
import io.github.cdimascio.dotenv.Dotenv;

// https://aistudio.google.com/
// https://aistudio.google.com/api-keys
public class AIService {
    private final Client client;

    public AIService() {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GOOGLE_API_KEY");
        client = Client.builder().apiKey(apiKey).build();
    }

    public String chat(String text) {
        return client.models.generateContent(
                "gemini-2.5-flash", text, null).text();
    }
}
