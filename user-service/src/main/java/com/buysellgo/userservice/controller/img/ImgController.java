package com.buysellgo.userservice.controller.img;

import com.buysellgo.userservice.service.dto.ServiceRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.service.ImgService;
import com.buysellgo.userservice.common.exception.CustomException;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/img")
@RequiredArgsConstructor
public class ImgController {
    private final ImgService imgService;
    @Operation(summary = "사진 업로드")
    @PostMapping("/upload")
    public ResponseEntity<CommonResDto<Object>> upload(@RequestPart("file") MultipartFile file) {
        ServiceRes<Map<String, Object>> result = imgService.upload(file);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "파일 업로드 성공", result.data()));
    }

    @Operation(summary = "사진 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<CommonResDto<Object>> delete(@RequestBody() Map<String, String> data) {
        ServiceRes<Map<String, Object>> result = imgService.delete(data.get("filePath"));
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "파일 삭제 성공", result.data()));
    }

}
