package com.module5.zingMp3Clone.Service.Singer;

import com.module5.zingMp3Clone.Model.Request.SingerRequest;
import com.module5.zingMp3Clone.Model.Response.SingerResponse;

import java.util.List;

public interface ISingerService {
    List<SingerResponse> listSinger();
    SingerResponse getSingerById(String id);
    SingerResponse save(SingerRequest singerRequest);
    void deleteSingerByIds(List<String> id);
}
