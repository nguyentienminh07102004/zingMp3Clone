package com.module5.zingMp3Clone.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingerResponse {
    private String id;
    private String name;
    private String avatar;
}
