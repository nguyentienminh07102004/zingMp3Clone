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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/songs/all")
    public ResponseEntity<APIResponse> getAllSongs() {
        List<SongResponse> list = songService.getAllSongs();
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .data(list)
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping(value = "/songs/uploaded")
    public ResponseEntity<APIResponse> getMySongs() {
        List<SongResponse> responses = songService.getMySong();
        APIResponse apiResponse = APIResponse.builder()
                .message("SUCCESS")
                .data(responses)
                .build();
        return ResponseEntity.status(200).body(apiResponse);
    }

    public ResponseEntity<APIResponse> findSongBySingerAndName(@RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String singerId) {
        List<SongResponse> responses = songService.get
    }
}