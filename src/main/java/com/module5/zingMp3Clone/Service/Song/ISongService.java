package com.module5.zingMp3Clone.Service.Song;

import com.module5.zingMp3Clone.Model.Request.SongRequest;
import com.module5.zingMp3Clone.Model.Response.SongResponse;
import org.springframework.data.web.PagedModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ISongService {
    List<SongResponse> getAllSongs();
    PagedModel<SongResponse> getAllSongs(Integer page);
    SongResponse getSongById(String id);
    SongResponse saveSong(SongRequest song);
    void deleteSong(List<String> ids);
    List<SongResponse> getMySong();
    List<SongResponse> findByNameContaining(String name);
    List<SongResponse> findBySingerId(String singerId);
    @Transactional
    void increaseListenCount(String url);
    @Transactional
    void increaseLikeCount(String id);
    @Transactional
    void decreaseLikeCount(String id);


}
