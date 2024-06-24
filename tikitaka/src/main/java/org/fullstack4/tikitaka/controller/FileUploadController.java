package org.fullstack4.tikitaka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.config.S3Config;
import org.fullstack4.tikitaka.utils.FileUploadUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@Controller
public class FileUploadController {

    private final FileUploadUtil fileUploadUtil;

    @GetMapping("/upload")
    public void uploadGET() {}

    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        //원본 파일 이름
        String org_file_name = file.getOriginalFilename();

        //업로드된 파일 이름, "test"는 폴더명으로 필요한 폴더명으로 바꿔서 사용
        String fileName = fileUploadUtil.upload(file, "test");

        //업로드된 파일 url -- html 파일에서 이미지 불러올 때 사용
        // ex) <img src="dto.fileUrl">
        String fileUrl = fileUploadUtil.getFileUrl(fileName, "test");

        //DB에 저장된 파일 이름으로 조회해서 삭제
        fileUploadUtil.deleteFile(fileName, "test");

    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam(name="fileName") String fileName) throws IOException {
        return fileUploadUtil.download(fileName, "test");
    }
}
