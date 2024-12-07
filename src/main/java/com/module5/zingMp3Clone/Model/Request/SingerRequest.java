package com.module5.zingMp3Clone.Model.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class SingerRequest {
    private String id;
    @NotNull(message = "Name is not null")
    @NotBlank(message = "Name is not blank")
    private String name;
    private String avatar;
}
