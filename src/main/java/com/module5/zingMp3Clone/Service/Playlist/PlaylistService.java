package com.module5.zingMp3Clone.Service.Playlist;

import com.module5.zingMp3Clone.Model.Entity.PlaylistEntity;
import com.module5.zingMp3Clone.Model.Request.PlaylistRequest;
import com.module5.zingMp3Clone.Model.Response.PlaylistDetailResponse;
import com.module5.zingMp3Clone.Model.Response.PlaylistResponse;
import com.module5.zingMp3Clone.Repository.IPlaylistRepository;
import com.module5.zingMp3Clone.Util.GenerateSlug;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final IPlaylistRepository playlistRepository;
    private final ModelMapper modelMapper;

    public List<PlaylistResponse> getAllPlaylistsOfCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PlaylistEntity> playlistEntities = playlistRepository.findByCreatedBy(username);
        if (playlistEntities.isEmpty()) {return null;}
        return playlistEntities.stream()
                .map(playlistEntity -> modelMapper.map(playlistEntity, PlaylistResponse.class))
                .toList();
    }
    public PagedModel<PlaylistResponse> getAllPlaylists(Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<PlaylistEntity> playlists = playlistRepository.findAll(pageable);
        return new PagedModel<>(playlists.map(playlistEntity ->
                modelMapper.map(playlistEntity, PlaylistResponse.class)));
    }

    public PlaylistDetailResponse getPlaylistDetail(String slug) {
        PlaylistEntity playlistEntity = playlistRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Playlist not found!"));
        return modelMapper.map(playlistEntity, PlaylistDetailResponse.class);
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

}
