package com.module5.zingMp3Clone.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongRequest {
    private String id;
    private String name;
    private List<String> singers;
    private String description;
    private String url;
    private String avatar;
}
