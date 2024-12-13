package com.module5.zingMp3Clone.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistResponse {
    private String id;
    private String name;
    private String avatar;
    private String slug;
    private String createdBy;
}
