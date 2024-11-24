package com.module5.zingMp3Clone.Service.Song;

import com.module5.zingMp3Clone.Exception.DataInvalidException;
import com.module5.zingMp3Clone.Model.Entity.SingerEntity;
import com.module5.zingMp3Clone.Model.Entity.SongEntity;
import com.module5.zingMp3Clone.Model.Request.SongRequest;
import com.module5.zingMp3Clone.Model.Response.SingerResponse;
import com.module5.zingMp3Clone.Model.Response.SongResponse;
import com.module5.zingMp3Clone.Repository.ISingerRepository;
import com.module5.zingMp3Clone.Repository.ISongRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SongServiceImpl implements ISongService {
    ISongRepository songRepository;
    ModelMapper modelMapper;
    ISingerRepository singerRepository;

    @Override
    public List<SongResponse> getAllSongs() {
        List<SongEntity> list = songRepository.findAll();
        List<SongResponse> responseList = new ArrayList<>();
        for (SongEntity entity : list) {
            SongResponse songResponse = toResponse(entity);
            responseList.add(songResponse);
        }
        return responseList;
    }

    @Override
    public PagedModel<SongResponse> getAllSongs(Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<SongEntity> list = songRepository.findAll(pageable);
        Page<SongResponse> response = list.map(this::toResponse);
        return new PagedModel<>(response);
    }

    @Override
    public SongResponse getSongById(String id) {
        SongEntity songEntity = songRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException("Song not found!"));
        return toResponse(songEntity);
    }

    @Override
    @Transactional
    public SongResponse saveSong(SongRequest song) {
        if (song.getId() != null) {
            this.getSongById(song.getId());
        }
        SongEntity songEntity = toEntity(song);
        SongEntity result = songRepository.save(songEntity);
        return toResponse(result);
    }

    @Override
    @Transactional
    public void deleteSong(List<String> ids) {
        for (String id : ids) {
            songRepository.findById(id)
                    .orElseThrow(() -> new DataInvalidException("Song not found!"));
            songRepository.deleteById(id);
        }
    }

    private SongResponse toResponse(SongEntity entity) {
        SongResponse songResponse = modelMapper.map(entity, SongResponse.class);
        List<SingerResponse> singerResponses = entity.getSingers().stream()
                .map(singer -> modelMapper.map(singer, SingerResponse.class))
                .toList();
        songResponse.setSingers(singerResponses);
        return songResponse;
    }

    private SongEntity toEntity(SongRequest request) {
        SongEntity entity = modelMapper.map(request, SongEntity.class);
        List<String> singers = request.getSingers();
        List<SingerEntity> list = new ArrayList<>();
        for (String singer : singers) {
            SingerEntity singerEntity = singerRepository.findById(singer)
                    .orElseThrow(() -> new DataInvalidException("Singer not found!"));
            list.add(singerEntity);
        }
        entity.setSingers(list);
        return entity;
    }
}