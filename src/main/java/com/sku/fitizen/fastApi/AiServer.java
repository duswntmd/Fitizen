package com.sku.fitizen.fastApi;

import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ai")
public class AiServer {

    private final HttpClient client;
//    private final String pythonServerUrl = "http://220.67.113.235:8000/";
    private final String pythonServerUrl = "http://127.0.0.1:8000";
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






    @PostMapping("/predict_exercise")
    @ResponseBody
    public ResponseEntity<String> recommendExercise(@RequestBody Map<String, String> requestData,HttpSession session,Model model) {
        try {
            // 데이터 변환 및 JSON 객체 생성
            Map<String, Object> data = new HashMap<>();
            data.put("height", Integer.parseInt(requestData.get("height")));
            data.put("weight", Integer.parseInt(requestData.get("weight")));
            data.put("goal", requestData.get("goal"));
            data.put("frequency", requestData.get("frequency"));
            data.put("preference", requestData.get("preference"));

            JSONObject json = new JSONObject(data);  // 'data' 사용하여 JSON 생성
            System.out.println("JSON to be sent to Python server: " + json.toString());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(pythonServerUrl + "/predict_exercise"))
                    .header("Content-Type", "application/json")
                    .version(HttpClient.Version.HTTP_1_1)  // HTTP/1.1로 강제 설정
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response from Python server: " + response.body());
            String responseBody = response.body();
            JSONObject jsonObject = new JSONObject(responseBody);
            String exercise = jsonObject.getString("recommended_exercise");
            System.out.println("추천운동:" + exercise);


            // Model 객체에 값 저장
            session.setAttribute("exercise", exercise);

            // JSON 형태로 반환

            return ResponseEntity.ok("/aiResult");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/aiResult")
    public String aiResult(HttpSession session, Model model) {
        String exercise = (String) session.getAttribute("exercise"); // 세션에서 값 가져오기
        model.addAttribute("exercise", exercise); // 모델에 값 추가
        return "aiResult";
    }


}

