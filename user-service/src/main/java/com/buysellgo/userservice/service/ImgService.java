package com.buysellgo.userservice.service;

import com.buysellgo.userservice.service.dto.ServiceRes;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImgService {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucketName}")
    private String bucketName;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    public ServiceRes<Map<String, Object>> upload(MultipartFile file) {
        Map<String, Object> data = new HashMap<>();
        try {
            InputStream inputStream = file.getInputStream();
            String objectName = file.getOriginalFilename();

            log.info("objectName: {}", objectName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String url = endpoint + "/" + bucketName + "/" + objectName;

            data.put("url", url);
            return ServiceRes.success("파일 업로드 성공",data);
        } catch (Exception e) {
            data.put("error", e.getMessage());
            return ServiceRes.fail("파일 업로드 실패",data);
        }
    }

    public ServiceRes<Map<String, Object>> delete(String filePath) {
        Map<String, Object> data = new HashMap<>();
        try {

            log.info("Received filePath: {}", filePath);

            String objectName = filePath.replace(endpoint + "/" + bucketName + "/", "");

            log.info("Attempting to delete object: {} from bucket: {}", objectName, bucketName);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

            return ServiceRes.success("파일 삭제 성공", data);
        } catch (Exception e) {
            log.error("Error deleting object: {}", e.getMessage(), e);
            data.put("error", e.getMessage());
            return ServiceRes.fail("파일 삭제 실패", data);
        }
    }
}
