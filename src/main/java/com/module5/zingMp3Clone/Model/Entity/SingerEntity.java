package com.module5.zingMp3Clone.Model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class SingerEntity extends BaseEntity {
    @Id
    private String id;
    private String name;
    private String avatar;

    @ManyToMany
    private List<SongEntity> songs;
}
