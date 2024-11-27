package com.module5.zingMp3Clone.API;

import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.module5.zingMp3Clone.Model.Response.PlaylistDetailResponse;
import com.module5.zingMp3Clone.Model.Response.PlaylistResponse;
import com.module5.zingMp3Clone.Model.Response.SongResponse;
import com.module5.zingMp3Clone.Service.Playlist.PlaylistService;
import com.module5.zingMp3Clone.Service.Song.ISongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
    private final PlaylistService playlistService;
    private final ISongService songService;

    @GetMapping("/songs")
    public ResponseEntity<APIResponse> getSongs(@RequestParam(defaultValue = "0", required = false) Integer page) {
        PagedModel<SongResponse> songs = songService.getAllSongs(page);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(songs)
                .build());
    }


    @GetMapping("/all-playlists")
    public ResponseEntity<APIResponse> getAllPlaylists(@RequestParam(defaultValue = "0", required = false) Integer page) {
        PagedModel<PlaylistResponse> playlists = playlistService.getAllPlaylists(page);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(playlists)
                .build());
    }
    @GetMapping("/user-playlists")
    public ResponseEntity<APIResponse> getUserPlaylists() {
        List<PlaylistResponse> playlists = playlistService.getAllPlaylistsOfCurrentUser();
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(playlists)
                .build());
    }

    @GetMapping("/playlist-detail/{slug}")
    public ResponseEntity<APIResponse> getPlaylistDetail(@PathVariable String slug) {
        PlaylistDetailResponse playlistDetail = playlistService.getPlaylistDetail(slug);
        return ResponseEntity.ok(APIResponse.builder()
                .message("SUCCESS")
                .data(playlistDetail)
                .build());
    }

}
