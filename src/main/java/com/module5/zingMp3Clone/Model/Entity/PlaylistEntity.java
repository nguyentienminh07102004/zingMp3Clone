package com.module5.zingMp3Clone.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "playlists")
@Getter
@Setter
public class PlaylistEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "slug")
    private String slug;
    @Column(name = "avatar", columnDefinition = "LONGTEXT")
    private String avatar;
    @Column(name = "view_counts")
    private Long viewCounts;
    @Column(name = "like_counts")
    private Long likeCounts;

    @ManyToMany(mappedBy = "playlists")
    private List<SongEntity> songs;
}
