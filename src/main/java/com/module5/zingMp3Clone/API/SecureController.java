package com.module5.zingMp3Clone.API;

import com.module5.zingMp3Clone.Model.Request.PlaylistRequest;
import com.module5.zingMp3Clone.Model.Request.SongRequest;
import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.module5.zingMp3Clone.Model.Response.PlaylistResponse;
import com.module5.zingMp3Clone.Model.Response.SongResponse;
import com.module5.zingMp3Clone.Service.Playlist.PlaylistService;
import com.module5.zingMp3Clone.Service.Song.ISongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secure")
@RequiredArgsConstructor
public class SecureController {
    private final ISongService songService;
    private final PlaylistService playlistService;

    @PostMapping("new-song")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<APIResponse> addSong(@RequestBody SongRequest song) {
        SongResponse songResponse = songService.saveSong(song);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(songResponse)
                .build());
    }

    @PostMapping("new-playlist")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<APIResponse> addPlaylist(@RequestBody PlaylistRequest playlist) {
        PlaylistResponse playlistResponse = playlistService.savePlaylist(playlist);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(playlistResponse)
                .build());
    }
}
