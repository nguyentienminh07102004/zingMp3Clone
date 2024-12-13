package com.module5.zingMp3Clone.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongResponse {
    private String id;
    private String name;
    private String avatar;
    private String url;
    private List<SingerResponse> singers;
    private Long numsOfLike;
    private Long numsOfListen;
    private boolean liked;
    private String description;
    private LocalDate createdDate;
    private String createdBy;
}
