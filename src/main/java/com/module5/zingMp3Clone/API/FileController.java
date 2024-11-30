package com.module5.zingMp3Clone.API;

import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.module5.zingMp3Clone.Service.GoogleDrive.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final GoogleDriveService googleDriveService;

    @PostMapping("/upload")
    public ResponseEntity<APIResponse> upload(@RequestParam("file") MultipartFile file) {
        String fileID;
        try {
            fileID = googleDriveService.uploadFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(fileID)
                .build());
    }
}
