package com.sku.fitizen.service;

import com.sku.fitizen.domain.BoardFilesVO;
import com.sku.fitizen.mapper.FileMapper;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final Path fileStorageLocation;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    public FileService(ServletContext context) {
        // /WEB-INF/files 경로로 파일을 저장하기 위한 절대 경로 설정
        this.fileStorageLocation = Paths.get(context.getRealPath("/WEB-INF/files"))
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
    public void storeFileOrYoutube(String youtubeUrl, MultipartFile file, Long bno) {
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
}
