package com.module5.zingMp3Clone.API;

import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.module5.zingMp3Clone.Model.Response.SingerResponse;
import com.module5.zingMp3Clone.Service.Singer.SingerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/singer")
@RequiredArgsConstructor
public class SingerController {
    private final SingerService singerService;

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllSinger() {
        List<SingerResponse> singerResponses = singerService.getAllSingers();
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(singerResponses)
                .build());
    }
    @GetMapping("/search")
    public ResponseEntity<APIResponse> search(@RequestParam("singerName") String name) {
        List<SingerResponse> singerResponses = singerService.getAllSingersByName(name);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(singerResponses)
                .build());
    }
}
