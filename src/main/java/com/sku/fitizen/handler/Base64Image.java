package com.sku.fitizen.handler;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class Base64Image {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void saveBase64ToImage(String img, String UUIDImgName) throws IOException {
        // Base64 문자열에서 prefix 제거 (예: data:image/png;base64,)
        String base64ImageString = img.replaceFirst("data:image/[^;]*;base64,", "");

        // Base64 문자열을 바이트 배열로 변환
        byte[] imageBytes = Base64.getDecoder().decode(base64ImageString);

        // 파일 경로 설정
        File file = new File(uploadDir+"chatImg/" + UUIDImgName);

        // 이미지 파일로 저장
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }
    }

    public void saveToProofShotBoard(String img, String UUIDImgName) throws IOException {
        // Base64 문자열에서 prefix 제거 (예: data:image/png;base64,)
        String base64ImageString = img.replaceFirst("data:image/[^;]*;base64,", "");

        // Base64 문자열을 바이트 배열로 변환
        byte[] imageBytes = Base64.getDecoder().decode(base64ImageString);

        // 파일 경로 설정
        File file = new File(uploadDir+"proofShot/" + UUIDImgName);

        // 이미지 파일로 저장
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }
    }











    // UUID로 저장된 이미지를 Base64로 변환하는 메서드
    public String imageToBase64(String uuidImgName) throws IOException {
        // 이미지 파일 경로 설정
        File imgFile = new File(uploadDir+"chatImg/"+ uuidImgName);

        // 파일을 바이트 배열로 읽어들임
        byte[] imageBytes = Files.readAllBytes(imgFile.toPath());

        // Base64로 인코딩
        return Base64.getEncoder().encodeToString(imageBytes);
    }


}
