package com.module5.zingMp3Clone.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "songs")
@Getter
@Setter
public class SongEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "avatar", columnDefinition = "LONGTEXT")
    private String avatar;
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;
    @Column(name = "url")
    private String url;
    @Column(name = "nums_of_listen")
    private Long numsOfListen;
    @Column(name = "nums_of_like")
    private Long numsOfLike;

    @ManyToMany
    @JoinTable(name = "song_singer",
    joinColumns = @JoinColumn(name = "song_id"),
    inverseJoinColumns = @JoinColumn(name = "singer_id"))
    private List<SingerEntity> singers;

    @ManyToMany(mappedBy = "songs")
    private List<PlaylistEntity> playlists;
}
