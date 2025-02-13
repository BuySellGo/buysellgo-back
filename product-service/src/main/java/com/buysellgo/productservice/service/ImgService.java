package com.buysellgo.productservice.service;

import com.buysellgo.productservice.service.dto.ServiceResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImgService {

    @Value("${aws.s3.endpoint}")
    private String endpoint;
    @Value("${aws.s3.accessKey}")
    private String accessKey;
    @Value("${aws.s3.secretKey}")
    private String secretKey;
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public ServiceResult<Map<String, Object>> upload(MultipartFile file) {
        Map<String, Object> data = new HashMap<>();
        try {
            String objectName = file.getOriginalFilename();
            log.info("objectName: {}", objectName);
    
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectName)
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
    
            // S3 퍼블릭 URL 생성
            String url = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + objectName;
            data.put("url", url);
            return ServiceResult.success("파일 업로드 성공", data);
        } catch (Exception e) {
            log.error("Error uploading object: {}", e.getMessage(), e);
            data.put("error", e.getMessage());
            return ServiceResult.fail("파일 업로드 실패", data);
        }
    }

    public ServiceResult<Map<String, Object>> delete(String filePath) {
        Map<String, Object> data = new HashMap<>();
        try {
            log.info("Received filePath: {}", filePath);
            
            // URL에서 객체 키 추출
            String objectName = filePath.replace("https://" + bucketName + ".s3." + region + ".amazonaws.com/", "");
            log.info("Attempting to delete object: {} from bucket: {}", objectName, bucketName);
    
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectName)
                            .build()
            );
    
            return ServiceResult.success("파일 삭제 성공", data);
        } catch (Exception e) {
            log.error("Error deleting object: {}", e.getMessage(), e);
            data.put("error", e.getMessage());
            return ServiceResult.fail("파일 삭제 실패", data);
        }
    }
}
