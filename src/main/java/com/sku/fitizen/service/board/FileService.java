package com.sku.fitizen.service.board;

import com.sku.fitizen.domain.board.BoardFilesVO;
import com.sku.fitizen.mapper.board.FileMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final Path fileStorageLocation;
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
        this.fileStorageLocation = Paths.get("src/main/resources/static/boardimages")
                .toAbsolutePath().normalize();
        try {
            // 파일 저장할 디렉토리가 없으면 생성
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("파일을 저장할 디렉토리를 생성할 수 없습니다.", ex);
        }
    }


    // 파일 저장 경로를 반환하는 메서드
    public Path getFileStorageLocation() {
        return this.fileStorageLocation;
    }

    // 파일 업로드 처리
    @Transactional
    public String storeFileOrYoutube(String youtubeUrl, MultipartFile file, Long bno) {
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            // 유튜브 URL 저장
            BoardFilesVO fileEntity = new BoardFilesVO();
            fileEntity.setBno(bno);
            fileEntity.setYoutubeUrl(youtubeUrl);
            fileMapper.insertFile(fileEntity);
        }

        if (file != null && !file.isEmpty()) {
            // 파일 저장
            String originalFilename = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String fileNameWithUUID = uuid + "_" + originalFilename;

            try {
                Path targetLocation = this.fileStorageLocation.resolve(fileNameWithUUID);
                Files.copy(file.getInputStream(), targetLocation);

                BoardFilesVO fileEntity = new BoardFilesVO();
                fileEntity.setBno(bno);
                fileEntity.setRealName(originalFilename);
                fileEntity.setUuidName(fileNameWithUUID);
                fileEntity.setFsize(file.getSize());
                fileEntity.setFtype(file.getContentType());

                fileMapper.insertFile(fileEntity);
            } catch (IOException ex) {
                throw new RuntimeException("파일을 저장할 수 없습니다. " + originalFilename, ex);
            }
        }
        return null;
    }

    // 특정 게시글에 속한 파일 목록 조회
    public List<BoardFilesVO> getFilesByBoard(Long bno) {
        return fileMapper.getFilesByBoard(bno);
    }

    public BoardFilesVO getFileByFnum(Long fnum) {
        return fileMapper.getFileByFnum(fnum);
    }


    // 파일을 리소스로 로드
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("파일을 찾을 수 없습니다. " + fileName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("파일을 찾을 수 없습니다. " + fileName, ex);
        }
    }

    // 파일 삭제 처리
    @Transactional
    public boolean deleteFileByFnum(Long fnum) {
        BoardFilesVO fileToDelete = fileMapper.getFileByFnum(fnum);

        if (fileToDelete == null) {
            return false;
        }

        try {
            // 유튜브 링크인 경우
            if (fileToDelete.getYoutubeUrl() != null && !fileToDelete.getYoutubeUrl().isEmpty()) {
                fileMapper.deleteFile(fnum);  // 데이터베이스에서 유튜브 링크만 삭제
                return true;
            }

            // 파일인 경우
            Path filePath = this.fileStorageLocation.resolve(fileToDelete.getUuidName()).normalize();
            File file = filePath.toFile();

            if (file.exists()) {
                if (file.delete()) {
                    fileMapper.deleteFile(fnum);
                    return true;
                } else {
                    throw new IOException("파일 삭제 실패: " + fileToDelete.getUuidName());
                }
            } else {
                fileMapper.deleteFile(fnum);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 특정 게시글에 속한 모든 파일 삭제
    public void deleteFilesByBoard(Long bno) {
        // 게시글에 속한 파일 목록 조회
        List<BoardFilesVO> files = fileMapper.getFilesByBoard(bno);

        // 파일 삭제 로직
        if (files != null && !files.isEmpty()) {
            for (BoardFilesVO file : files) {
                deleteFileByFnum(file.getFnum());
            }
        }

        // 데이터베이스에서 해당 게시글과 연결된 파일 정보 삭제
        fileMapper.deleteFilesByBoard(bno);
    }

    public ResponseEntity<Resource> downloadFile(Long fnum) throws IOException {
        // 파일 정보 조회
        BoardFilesVO file = getFileByFnum(fnum);
        if (file == null) {
            throw new RuntimeException("해당 파일을 찾을 수 없습니다.");
        }

        // 파일 리소스를 로드
        Path filePath = getFileStorageLocation().resolve(file.getUuidName()).normalize();
        Resource resource = loadFileAsResource(file.getUuidName());

        // 리소스가 존재하면 응답 생성
        if (resource.exists()) {
            String encodedFileName = URLEncoder.encode(file.getRealName(), "UTF-8").replace("+", "%20");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(resource);
        } else {
            throw new IOException("파일을 찾을 수 없습니다.");
        }
    }

    public void validateFiles(List<MultipartFile> files) {
        if (files.size() > 5) {
            throw new IllegalArgumentException("파일은 최대 5개까지 업로드할 수 있습니다.");
        }

        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("video/")) {
                throw new IllegalArgumentException("동영상 파일은 업로드할 수 없습니다.");
            }
        }
    }

    public void validateFilesForUpdate(List<MultipartFile> files, List<Long> deleteFileIds, List<BoardFilesVO> existingFiles) {
        // 삭제되지 않은 이미지 개수 계산
        long remainingImageCount = existingFiles.stream()
                .filter(file -> file.getFtype() != null && file.getFtype().startsWith("image/") &&
                        (deleteFileIds == null || !deleteFileIds.contains(file.getFnum())))
                .count();

        // 새로 추가할 이미지 파일 개수
        long newImageFileCount = files.stream()
                .filter(file -> file.getContentType() != null && file.getContentType().startsWith("image/"))
                .count();

        int maxImageCount = 5; // 최대 이미지 개수
        if (remainingImageCount + newImageFileCount > maxImageCount) {
            throw new IllegalArgumentException("이미지는 최대 5개까지 추가할 수 있습니다. 현재 추가 가능한 이미지 수: " + (maxImageCount - remainingImageCount));
        }
    }

    public void validateYoutubeUrlsForUpdate(List<String> youtubeUrls, List<Long> deleteFileIds, List<BoardFilesVO> existingFiles) {
        // 삭제되지 않은 유튜브 URL 개수 계산
        long remainingYoutubeCount = existingFiles.stream()
                .filter(file -> file.getYoutubeUrl() != null && !file.getYoutubeUrl().isEmpty() &&
                        (deleteFileIds == null || !deleteFileIds.contains(file.getFnum())))
                .count();

        int maxYoutubeCount = 3; // 최대 유튜브 URL 개수
        if (youtubeUrls != null && remainingYoutubeCount + youtubeUrls.size() > maxYoutubeCount) {
            throw new IllegalArgumentException("유튜브 영상은 최대 3개까지 추가할 수 있습니다. 현재 추가 가능한 영상 수: " + (maxYoutubeCount - remainingYoutubeCount));
        }
    }


}
