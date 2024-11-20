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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/ai")
@SessionAttributes("user")
public class AiServer {

    private final HttpClient client;
    private final String pythonServerUrl = "http://127.0.0.1:8000/"; //박성재테스트용 파이썬url
    private final RestTemplate restTemplate = new RestTemplate();
    //private final String pythonServerUrl = "http://220.67.113.237:8000/";
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

        return "th/videoList"; // th/userVideos.html로 렌더링
    }

//    @PostMapping("/uploadProcessedVideo")
//    @ResponseBody
//    public ResponseEntity<Map<String, String>> uploadProcessedVideo(@RequestParam("file") MultipartFile file) {
//        String fileName = file.getOriginalFilename();
//        String directory = "src/main/resources/static/processed_videos/";
//        Map<String, String> response = new HashMap<>();
//
//        try {
//            // 지정된 디렉토리 확인 및 생성
//            Path directoryPath = Paths.get(directory);
//            if (!Files.exists(directoryPath)) {
//                Files.createDirectories(directoryPath);
//            }
//
//            // 파일 저장
//            Path filePath = directoryPath.resolve(fileName);
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            System.out.println("Video successfully saved to " + filePath.toString());
//            System.out.println("파일 이름: " + file.getOriginalFilename());
//            System.out.println("파일 크기: " + file.getSize());
//            response.put("message", "File uploaded successfully");
//            response.put("fileName", fileName);
//            return ResponseEntity.ok(response);  // JSON 응답 반환
//        } catch (IOException e) {
//            e.printStackTrace();
//            response.put("message", "Could not upload file");
//            response.put("error", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // 에러 시 JSON 응답 반환
//        }
//    }

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
            String fileUrl = "http://220.67.113.236/video_storage/" + uuidFilename;

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
                videoAnalysis.setVideourl(fileUrl);
                videoAnalysis.setAivideourl(aiVideoUrl);
                videoAnalysis.setVideoresult(analysisResult);


                videoAnalysisService.insertVideoAnalysis(videoAnalysis);
                videoAnalysisService.updateVideoAnalysisResult(videoAnalysis);
                model.addAttribute("aivideourl", aiVideoUrl);
                model.addAttribute("result", "분석완료");
            } else {
                model.addAttribute("error", "오류: " + response.statusCode() + " 상태 코드가 반환되었습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "오류 발생: " + e.getMessage());
        }

        return "th/uploadVideo";
    }



    // 특정 비디오 분석 데이터 조회
    @GetMapping("/video/{vnum}")
    public ResponseEntity<VideoAnalysis> getVideoAnalysisById(@PathVariable long vnum) {
        VideoAnalysis videoAnalysis = videoAnalysisService.getVideoAnalysisById(vnum);
        if (videoAnalysis != null) {
            return ResponseEntity.ok(videoAnalysis);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 모든 비디오 분석 데이터 조회
    @GetMapping("/videos")
    public ResponseEntity<List<VideoAnalysis>> getAllVideoAnalysis() {
        List<VideoAnalysis> videoAnalysisList = videoAnalysisService.getAllVideoAnalysis();
        return ResponseEntity.ok(videoAnalysisList);
    }

    // 비디오 분석 데이터 삭제
    @DeleteMapping("/video/{vnum}")
    public ResponseEntity<String> deleteVideoAnalysis(@PathVariable long vnum) {
        int result = videoAnalysisService.deleteVideoAnalysis(vnum);
        if (result > 0) {
            return ResponseEntity.ok("비디오 분석 데이터가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(500).body("삭제할 데이터가 존재하지 않거나 삭제 중 오류가 발생했습니다.");
        }
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
                    .uri(URI.create(pythonServerUrl + "predict_exercise"))
                    .header("Content-Type", "application/json")
                    .version(HttpClient.Version.HTTP_1_1)  // HTTP/1.1로 강제 설정
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response from Python server: " + response.body());
            String responseBody = response.body();
            JSONObject jsonObject = new JSONObject(responseBody);
            String exercise1 = jsonObject.getString("recommended_exercise1");
            float confidence_score1=jsonObject.getFloat("confidence_score1");
            String exercise2 = jsonObject.getString("recommended_exercise2");
            float confidence_score2=jsonObject.getFloat("confidence_score2");
            String exercise3 = jsonObject.getString("recommended_exercise3");
            float confidence_score3=jsonObject.getFloat("confidence_score3");
            //System.out.println("추천운동:" + exercise+", 정확도:"+100*confidence_score+"%" );


            // Model 객체에 값 저장
            session.setAttribute("exercise1", exercise1);
            session.setAttribute("confidence_score1", confidence_score1);
            session.setAttribute("exercise2", exercise2);
            session.setAttribute("confidence_score2", confidence_score2);
            session.setAttribute("exercise3", exercise3);
            session.setAttribute("confidence_score3", confidence_score3);
            // JSON 형태로 반환

            return ResponseEntity.ok("/aiResult");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/aiResult")
    public String aiResult(HttpSession session, Model model) {
        String exercise1 = (String) session.getAttribute("exercise1"); // 세션에서 값 가져오기
        float confidence_score1=(float) session.getAttribute("confidence_score1");
        String exercise2 = (String) session.getAttribute("exercise2"); // 세션에서 값 가져오기
        float confidence_score2=(float) session.getAttribute("confidence_score2");
        String exercise3 = (String) session.getAttribute("exercise3"); // 세션에서 값 가져오기
        float confidence_score3=(float) session.getAttribute("confidence_score3");
        model.addAttribute("exercise1", exercise1); // 모델에 값 추가
        model.addAttribute("confidence_score1",confidence_score1);
        model.addAttribute("exercise2", exercise2); // 모델에 값 추가
        model.addAttribute("confidence_score2",confidence_score2);
        model.addAttribute("exercise3", exercise3); // 모델에 값 추가
        model.addAttribute("confidence_score3",confidence_score3);
        return "aiResult";
    }


}

