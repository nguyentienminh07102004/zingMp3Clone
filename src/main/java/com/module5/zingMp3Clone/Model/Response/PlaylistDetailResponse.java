package com.module5.zingMp3Clone.Model.Response;

import com.module5.zingMp3Clone.Model.Entity.SongEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistDetailResponse {
    private String id;
    private String name;
    private String avatar;
    private String createdBy;
    private LocalDate createdDate;
    private Long viewCounts;
    private Long likeCounts;
    private List<SongEntity> songs;
}
