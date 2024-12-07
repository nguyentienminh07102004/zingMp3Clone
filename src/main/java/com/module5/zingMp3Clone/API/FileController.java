package com.module5.zingMp3Clone.API;

import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.module5.zingMp3Clone.Service.GoogleDrive.GoogleDriveService;
import com.module5.zingMp3Clone.Service.Song.ISongService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final GoogleDriveService googleDriveService;
    private final ISongService songService;

    @PostMapping("/upload")
    public ResponseEntity<APIResponse> upload(@RequestParam("file") MultipartFile file) {
        String result;
        try {
            result = googleDriveService.uploadFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(result)
                .build());
    }
    @GetMapping("/stream-audio/{fileId}")
    public void streamAudio(@PathVariable String fileId, HttpServletResponse response, HttpServletRequest request) {
        try {
            googleDriveService.streamFileToResponse(fileId, response, request);
            songService.increaseListenCount(fileId);
        } catch (IOException | GeneralSecurityException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
