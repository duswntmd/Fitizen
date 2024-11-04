package com.sku.fitizen.fastApi;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller
@RequestMapping("/ai")
public class AiServer {

    private final HttpClient client;
    private final String pythonServerUrl = "http://220.67.113.235:8000/";

    public AiServer() {
        this.client = HttpClient.newHttpClient();
    }

    @GetMapping("/chatBot")
    public String chatBot() {
        return "th/chatBot";
    }

    @PostMapping("/chatBot/{selectedModel}")
    @ResponseBody
    public String chatGptAsk(@RequestBody Map<String, String> request, @PathVariable String selectedModel) {


        // 모델 이름 검증
        if (!selectedModel.equals("ask") && !selectedModel.equals("ask2")) {
            return "Error: Invalid model selection. Choose either 'ask' (ChatGPT) or 'ask2' (Ollama).";
        }
        String question = request.get("question");
        String json = String.format("{\"question\": \"%s\"}", question);

        // HttpRequest 생성 (POST 요청)
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(pythonServerUrl + selectedModel))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        // 요청 전송 및 응답 받기
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // 응답 상태 코드 확인 및 응답 본문 처리
            if (response.statusCode() == 200) {
                // 서버 응답 내용 출력 (디버깅)
                // JSON 파싱
                JSONObject jsonResponse = new JSONObject(response.body());

                // "answer" 필드가 있는지 확인
                if (jsonResponse.has("answer")) {
                    //System.out.println(jsonResponse.getString("answer"));
                    return jsonResponse.getString("answer");
                } else {
                    return "Error: 'answer' field not found in response";
                }
            } else {
                return "Error: Received status code " + response.statusCode();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}

