package com.sku.fitizen.fastApi;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.VideoAnalysis;
import com.sku.fitizen.service.VideoAnalysisService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/ai")
@SessionAttributes("user")
public class AiServer {

    private final HttpClient client;
    //private final String pythonServerUrl = "http://127.0.0.1:8000/"; //박성재테스트용 파이썬url
    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonServerUrl = "http://220.67.113.237:8000/";
    private final String analyzeVideoUrl = "http://220.67.113.237:8000/videos/";
    private final String uuidUrl = "http://220.67.113.236/video_storage/";
    private final VideoAnalysisService videoAnalysisService;

    @Autowired
    public AiServer(VideoAnalysisService videoAnalysisService) {
        this.client = HttpClient.newHttpClient();
        this.videoAnalysisService = videoAnalysisService;
    }

    @GetMapping("/userVideos")
    public String userVideoList(@SessionAttribute(value = "user", required = false) User user, Model model) {// 로그인한 사용자 ID 가져오기
        // 사용자 ID를 기준으로 데이터 조회
        List<VideoAnalysis> videoList = videoAnalysisService.getVideosByUser(user.getId());
        model.addAttribute("videoList", videoList);

        return "th/videoList";
    }

    @GetMapping("/detailvideo/{vnum}")
    public String getVideoDetail(@PathVariable int vnum, @SessionAttribute(value = "user", required = false) User user, Model model) {
        String baseUrl = "/video_storage/";
        VideoAnalysis videoAnalysis = videoAnalysisService.getVideoAnalysisDetail(vnum, user.getId());
        String[] resultPairs = videoAnalysis.getVideoresult().split(",");
        List<Integer> frames = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        List<String> valuesLabels = new ArrayList<>();  // 라벨링된 자세 리스트

        // 자세 코드와 라벨 매핑
        Map<Integer, String> labelMapping = new HashMap<>();
        labelMapping.put(0, "정상 자세");
        labelMapping.put(1, "무릎 안좋은 자세");
        labelMapping.put(2, "앉은 자세");
        labelMapping.put(3, "허리 구부린 자세");

        for (String pair : resultPairs) {
            String[] parts = pair.split(":");
            frames.add(Integer.parseInt(parts[0])); // 프레임
            int value = Integer.parseInt(parts[1]); // 값
            values.add(value);
            valuesLabels.add(labelMapping.getOrDefault(value, "알 수 없는 자세"));  // 값에 대한 라벨을 추가
        }

        // 도넛 차트를 위한 라벨과 빈도 계산
        List<String> doughnutLabels = new ArrayList<>();
        List<Integer> doughnutValues = new ArrayList<>();

        Map<Integer, Integer> valueCounts = new HashMap<>();
        for (Integer value : values) {
            valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
        }

        // 실제 등장하는 코드만 doughnutLabels와 doughnutValues에 추가
        for (Map.Entry<Integer, Integer> entry : valueCounts.entrySet()) {
            Integer code = entry.getKey();
            Integer count = entry.getValue();

            // 등장하는 코드가 labelMapping에 존재할 때만 추가
            if (labelMapping.containsKey(code)) {
                doughnutLabels.add(labelMapping.get(code));  // 실제로 존재하는 코드의 이름만 추가
                doughnutValues.add(count);
            }
        }

        // 모델에 데이터 추가
        model.addAttribute("doughnutLabels", doughnutLabels);
        model.addAttribute("doughnutValues", doughnutValues);
        model.addAttribute("userid", videoAnalysis.getUserid());
        model.addAttribute("videoUrl", baseUrl + videoAnalysis.getUuidvideoname());
        model.addAttribute("aiVideoUrl", analyzeVideoUrl + videoAnalysis.getAivideourl());
        model.addAttribute("analysisResults", videoAnalysis.getVideoresult());
        model.addAttribute("frames", frames);
        model.addAttribute("values", values);
        model.addAttribute("valuesLabels", valuesLabels);  // 프레임별 라벨링된 자세 값 추가
        return "th/videoDetail";
    }


    // 업로드 페이지 제공
    @GetMapping("/uploadVideo")
    public String uploadVideoPage() {
        return "th/uploadVideo";  // resources/templates/uploadVideo.html
    }

    @PostMapping("/analyzeVideo")
    public String analyzeVideo(@SessionAttribute(value = "user", required = false) User user,
                               @RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "동영상을 선택해주세요.");
            return "th/uploadVideo";
        }

        try {
            // 1. 파일 저장 경로 설정
            String originalFilename = file.getOriginalFilename();
            String uuidFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            String staticFilePath = new File("src/main/resources/static/video_storage/" + uuidFilename).getAbsolutePath();
            String fileUrl = uuidUrl + uuidFilename;

            // 파일 저장
            file.transferTo(new File(staticFilePath));

            // 2. 파일 저장 상태 및 크기 확인
            Path path = Paths.get(staticFilePath);
            long expectedSize = file.getSize();
            int maxRetries = 50;
            int retries = 0;

            while (Files.size(path) < expectedSize) {
                Thread.sleep(100);
                retries++;
                if (retries >= maxRetries) {
                    throw new IOException("파일 저장이 완료되지 않았습니다.");
                }
            }

            // 3. HTTP 접근 가능 여부 확인
            boolean fileReady = false;
            retries = 0;
            while (!fileReady && retries < maxRetries) {
                try {
                    HttpClient tempClient = HttpClient.newHttpClient();
                    HttpRequest fileCheckRequest = HttpRequest.newBuilder()
                            .uri(URI.create(fileUrl))
                            .GET()
                            .build();
                    HttpResponse<String> fileCheckResponse = tempClient.send(fileCheckRequest, HttpResponse.BodyHandlers.ofString());
                    if (fileCheckResponse.statusCode() == 200) {
                        fileReady = true;
                    }
                } catch (Exception ex) {
                    Thread.sleep(200);  // 대기 시간 증가
                    retries++;
                }
            }

            if (!fileReady) {
                throw new IOException("파일이 웹 서버에서 제공되지 않습니다.");
            }

            // 4. FastAPI 서버로 분석 요청 전송 (HttpClient 사용)
            HttpClient client = HttpClient.newHttpClient();
            Map<String, String> requestPayload = new HashMap<>();
            requestPayload.put("video_url", fileUrl);
            String json = new JSONObject(requestPayload).toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(pythonServerUrl + "analyzevideo"))
                    .header("Content-Type", "application/json")
                    .version(HttpClient.Version.HTTP_1_1)  // HTTP/1.1로 강제 설정
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println("Response Status Code: " + response.statusCode());
//            System.out.println("Response Body: " + response.body());
            // 5. 분석 결과 DB 업데이트
            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());

                String analysisResult = jsonResponse.optString("analysis_results", "결과 없음");
                String aiVideoUrl = jsonResponse.optString("aivideourl", "URL 없음");

                VideoAnalysis videoAnalysis = new VideoAnalysis();
                videoAnalysis.setUserid(user.getId());
                videoAnalysis.setRealvideoname(originalFilename);
                videoAnalysis.setUuidvideoname(uuidFilename);
                videoAnalysis.setAivideourl(aiVideoUrl);
                videoAnalysis.setVideoresult(analysisResult);


                videoAnalysisService.insertVideoAnalysis(videoAnalysis);
                videoAnalysisService.updateVideoAnalysisResult(videoAnalysis);
                model.addAttribute("vnum", videoAnalysis.getVnum());
                model.addAttribute("aivideourl", analyzeVideoUrl + aiVideoUrl);
                model.addAttribute("result", "비디오 상세 보기를 눌러주세요");
            } else {
                model.addAttribute("error", "오류: " + response.statusCode() + " 상태 코드가 반환되었습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "오류 발생: " + e.getMessage());
        }

        return "th/uploadVideo";
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
                .version(HttpClient.Version.HTTP_1_1)
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
                    .uri(URI.create(pythonServerUrl + "predict_exercise"))
                    .header("Content-Type", "application/json")
                    .version(HttpClient.Version.HTTP_1_1)  // HTTP/1.1로 강제 설정
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response from Python server: " + response.body());
            String responseBody = response.body();
            JSONObject jsonObject = new JSONObject(responseBody);
            String exercise = jsonObject.getString("recommended_exercise");
            float confidence_score=jsonObject.getFloat("confidence_score");
            System.out.println("추천운동:" + exercise+", 정확도:"+100*confidence_score+"%" );


            // Model 객체에 값 저장
            session.setAttribute("exercise", exercise);
            session.setAttribute("confidence_score", confidence_score);
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
        float confidence_score=(float) session.getAttribute("confidence_score");
        model.addAttribute("exercise", exercise); // 모델에 값 추가
        model.addAttribute("confidence_score",confidence_score*100);
        return "aiResult";
    }


}

