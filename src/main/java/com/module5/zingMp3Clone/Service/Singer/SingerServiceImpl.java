package com.module5.zingMp3Clone.Service.Singer;

import com.module5.zingMp3Clone.Exception.DataInvalidException;
import com.module5.zingMp3Clone.Model.Entity.SingerEntity;
import com.module5.zingMp3Clone.Model.Request.SingerRequest;
import com.module5.zingMp3Clone.Model.Response.SingerResponse;
import com.module5.zingMp3Clone.Repository.ISingerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SingerServiceImpl implements ISingerService {
    private final ISingerRepository singerRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SingerResponse> listSinger() {
        List<SingerEntity> singerList = singerRepository.findAll();
        return singerList.stream()
                .map(item -> modelMapper.map(item, SingerResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SingerResponse getSingerById(String id) {
        SingerEntity singerEntity = singerRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException("Id is not exists!"));
        return modelMapper.map(singerEntity, SingerResponse.class);
    }

    @Override
    @Transactional
    public SingerResponse save(SingerRequest singerRequest) {
        if (singerRequest.getId() != null) {
            this.getSingerById(singerRequest.getId());
        }
        SingerEntity singerEntity = modelMapper.map(singerRequest, SingerEntity.class);
        return modelMapper.map(singerRepository.save(singerEntity), SingerResponse.class);
    }

    @Override
    @Transactional
    public void deleteSingerByIds(List<String> ids) {
        ids.forEach(id -> {
            this.getSingerById(id);
            singerRepository.deleteById(id);
        });
    }
}
