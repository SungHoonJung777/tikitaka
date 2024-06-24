package org.fullstack4.tikitaka.utils;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileUploadUtil {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile, String folder) throws IOException {
        String org_file_name = "";
        String newName = "";

        try {
            org_file_name = multipartFile.getOriginalFilename();
            String ext = org_file_name.substring(org_file_name.lastIndexOf("."), org_file_name.length());

            UUID uuid = UUID.randomUUID();
            String[] uuids = uuid.toString().split("-");
            String fileName = "tikitaka/" + folder + "/" + uuids[0] + ext;
            newName = uuids[0] + ext;

            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());

            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objMeta);
        } catch(Exception e) {
            log.info(e.getMessage());
        }

        return newName;
    }

    public String getFileUrl(String fileName, String folder) {
        return amazonS3.getUrl(bucket, "tikitaka/" + folder + "/" + fileName).toString();
    }

    public void deleteFile(String fileName, String folder) throws IOException {
        try {
            amazonS3.deleteObject(bucket, "tikitaka/" + folder + "/" + fileName);
        } catch (SdkClientException e) {
            throw new IOException("Error deleting file from S3", e);
        }
    }

    public ResponseEntity<byte[]> download(String storedFileName, String folder) {
        try {
            S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, "tikitaka/" + folder + "/" + storedFileName));
            S3ObjectInputStream objectInputStream = o.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
        } catch (IOException | SdkClientException e) {
            log.error("Error downloading file: " + storedFileName, e);
            throw new FileDownloadFailedException("파일 다운로드에 실패했습니다: " + storedFileName, e);
        }
    }
}
