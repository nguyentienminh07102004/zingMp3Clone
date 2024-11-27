package com.module5.zingMp3Clone.Repository;

import com.module5.zingMp3Clone.Model.Entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPlaylistRepository extends JpaRepository<PlaylistEntity, String> {
    List<PlaylistEntity> findByCreatedBy(String username);
    Optional<PlaylistEntity> findBySlug(String slug);
    int countByName(String name);
}
