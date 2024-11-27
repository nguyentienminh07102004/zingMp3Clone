package com.module5.zingMp3Clone.Model.Request;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistRequest {
    private String id;
    private String name;
    private String avatar;
}
