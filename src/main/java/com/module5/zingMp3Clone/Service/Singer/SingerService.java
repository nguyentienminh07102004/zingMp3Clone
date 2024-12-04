package com.module5.zingMp3Clone.Service.Singer;

import com.module5.zingMp3Clone.Model.Entity.SingerEntity;
import com.module5.zingMp3Clone.Model.Response.SingerResponse;
import com.module5.zingMp3Clone.Repository.ISingerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SingerService {
    private final ISingerRepository singerRepository;
    private final ModelMapper modelMapper;

    public List<SingerResponse> getAllSingers() {
        List<SingerEntity> singerEntities = singerRepository.findAll();
        return singerEntities.stream()
                .map(entity -> modelMapper.map(entity, SingerResponse.class))
                .toList();
    }
    public List<SingerResponse> getAllSingersByName(String name) {
        List<SingerEntity> singerEntities = singerRepository.findByNameContaining(name);
        return singerEntities.stream()
                .map(entity -> modelMapper.map(entity, SingerResponse.class))
                .toList();
    }
}
