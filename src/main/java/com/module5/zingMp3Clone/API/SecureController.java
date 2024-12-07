package com.module5.zingMp3Clone.API;

import com.module5.zingMp3Clone.Model.Request.PlaylistRequest;
import com.module5.zingMp3Clone.Model.Request.SingerRequest;
import com.module5.zingMp3Clone.Model.Request.SongRequest;
import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.module5.zingMp3Clone.Model.Response.PlaylistResponse;
import com.module5.zingMp3Clone.Model.Response.SingerResponse;
import com.module5.zingMp3Clone.Model.Response.SongResponse;
import com.module5.zingMp3Clone.Service.Playlist.PlaylistService;
import com.module5.zingMp3Clone.Service.Singer.ISingerService;
import com.module5.zingMp3Clone.Service.Song.ISongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/secure")
@RequiredArgsConstructor
public class SecureController {
    private final ISongService songService;
    private final PlaylistService playlistService;
    private final ISingerService singerService;

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

    @PostMapping(value = "/singers/")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<APIResponse> saveSinger(@Valid @RequestBody SingerRequest singer) {
        SingerResponse singerResponse = singerService.save(singer);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .data(singerResponse)
                .build();
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping(value = "/singers/")
    public ResponseEntity<APIResponse> getAllSinger() {
        List<SingerResponse> list = singerService.listSinger();
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .data(list)
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping(value = "/singes/{ids}")
    public ResponseEntity<APIResponse> deleteByIds(@PathVariable List<String> ids) {
        singerService.deleteSingerByIds(ids);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/like/{songId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<APIResponse> likeSong(@PathVariable String songId) {
        PlaylistResponse playlistResponse = playlistService.addSongToPlaylist("default", songId);
        songService.increaseLikeCount(songId);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(playlistResponse)
                .build());
    }

    @PostMapping("/unlike/{songId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<APIResponse> unlikeSong(@PathVariable String songId) {
        PlaylistResponse playlistResponse = playlistService.removeSongFromPlaylist("default", songId);
        songService.decreaseLikeCount(songId);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(playlistResponse)
                .build());
    }

    @PostMapping("/add-song-to-playlist")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<APIResponse> addSongToPlaylist(@RequestBody Map<String, String> data) {
        String songId = data.get("songId");
        String playlistId = data.get("playlistId");
        PlaylistResponse playlistResponse = playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(playlistResponse)
                .build());
    }

    @PostMapping("/remove-song-from-playlist")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<APIResponse> removeSongFromPlaylist(@RequestBody Map<String, String> data) {
        String songId = data.get("songId");
        String playlistId = data.get("playlistId");
        PlaylistResponse playlistResponse = playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(playlistResponse)
                .build());
    }
}
