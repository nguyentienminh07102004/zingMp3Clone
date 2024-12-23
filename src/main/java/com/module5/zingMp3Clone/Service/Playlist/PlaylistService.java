package com.module5.zingMp3Clone.Service.Playlist;

import com.module5.zingMp3Clone.Exception.DataInvalidException;
import com.module5.zingMp3Clone.Model.Entity.PlaylistEntity;
import com.module5.zingMp3Clone.Model.Entity.SongEntity;
import com.module5.zingMp3Clone.Model.Request.PlaylistRequest;
import com.module5.zingMp3Clone.Model.Response.PlaylistDetailResponse;
import com.module5.zingMp3Clone.Model.Response.PlaylistResponse;
import com.module5.zingMp3Clone.Repository.IPlaylistRepository;
import com.module5.zingMp3Clone.Repository.ISongRepository;
import com.module5.zingMp3Clone.Repository.IUserRepository;
import com.module5.zingMp3Clone.Util.GenerateSlug;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final IPlaylistRepository playlistRepository;
    private final ModelMapper modelMapper;
    private final ISongRepository songRepository;
    private final IUserRepository userRepository;

    public List<PlaylistResponse> getAllPlaylistsOfCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String createdBy = userRepository.findByEmail(username).get().getUsername();
        List<PlaylistEntity> playlistEntities = playlistRepository.findByCreatedBy(username);
        if (playlistEntities.isEmpty()) {
            return null;
        }
        return playlistEntities.stream()
                .map(playlistEntity -> {
                    PlaylistResponse response = modelMapper.map(playlistEntity, PlaylistResponse.class);
                    response.setCreatedBy(createdBy);
                    return response;
                })
                .toList();
    }

    public PagedModel<PlaylistResponse> getAllPlaylists(Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<PlaylistEntity> playlists = playlistRepository.findAllByPlaylistDefaultFalse(pageable);
        return new PagedModel<>(playlists.map(playlistEntity -> {
            PlaylistResponse playlistResponse = modelMapper.map(playlistEntity, PlaylistResponse.class);
            String createdBy = userRepository.findByEmail(playlistResponse.getCreatedBy()).get().getUsername();
            playlistResponse.setCreatedBy(createdBy);
            return playlistResponse;
        }));
    }

    public PlaylistDetailResponse getPlaylistDetail(String slug) {
        if(slug == null || slug.isBlank()) slug = "default";
        PlaylistEntity playlistEntity = playlistRepository.findBySlug(slug)
                .orElseThrow(() -> new DataInvalidException("Playlist not found!"));
        PlaylistDetailResponse response = modelMapper.map(playlistEntity, PlaylistDetailResponse.class);
        String createdBy = userRepository.findByEmail(playlistEntity.getCreatedBy()).get().getUsername();
        response.setCreatedBy(createdBy);
        return response;
    }

    public PlaylistResponse savePlaylist(PlaylistRequest playlistRequest) {
        PlaylistEntity playlistEntity = modelMapper.map(playlistRequest, PlaylistEntity.class);
        playlistEntity.setViewCounts(0L);
        playlistEntity.setLikeCounts(0L);
        String slug = GenerateSlug.toSlug(playlistEntity.getName());
        int countByName = playlistRepository.countByName(playlistEntity.getName());
        if (countByName > 0) {
            slug += "-" + countByName;
        }
        playlistEntity.setSlug(slug);
        return modelMapper.map(playlistRepository.save(playlistEntity), PlaylistResponse.class);
    }

    public void createFavoritesPlaylist(String username) {
        PlaylistEntity playlist = new PlaylistEntity();
        playlist.setName(username + " Favorites");
        playlist.setSlug(GenerateSlug.toSlug(playlist.getName()));
        playlist.setCreatedBy(username);
        playlist.setCreatedDate(LocalDate.now());
        playlist.setViewCounts(0L);
        playlist.setPlaylistDefault(true);
        playlistRepository.save(playlist);
    }

    public PlaylistEntity getPlaylistById(String playlistId) {
        if (playlistId.equals("default")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return playlistRepository.findByPlaylistDefaultAndCreatedBy(true, username);
        }
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found!"));
    }

    public PlaylistResponse addSongToPlaylist(String playlistId, String songId) {
        PlaylistEntity playlist = this.getPlaylistById(playlistId);
        SongEntity song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found!"));
        if (playlist.getSongs().contains(song)) {
            throw new DataInvalidException("Song already exists in the playlist");
        }
        song.getPlaylists().add(playlist);
        playlist.getSongs().add(song);
        playlist = playlistRepository.save(playlist);
        return modelMapper.map(playlist, PlaylistResponse.class);
    }

    public PlaylistResponse removeSongFromPlaylist(String playlistId, String songId) {
        PlaylistEntity playlist = this.getPlaylistById(playlistId);
        SongEntity songToRemove = playlist.getSongs().stream()
                .filter(song -> song.getId().equals(songId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Song not found!"));
        songToRemove.getPlaylists().remove(playlist);
        playlist.getSongs().remove(songToRemove);
        playlist = playlistRepository.save(playlist);
        return modelMapper.map(playlist, PlaylistResponse.class);
    }
}
