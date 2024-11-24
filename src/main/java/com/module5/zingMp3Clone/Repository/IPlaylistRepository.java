package com.module5.zingMp3Clone.Repository;

import com.module5.zingMp3Clone.Model.Entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlaylistRepository extends JpaRepository<PlaylistEntity, String> {
}
