package com.module5.zingMp3Clone.Repository;

import com.module5.zingMp3Clone.Model.Entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISongRepository extends JpaRepository<SongEntity, String> {
    SongEntity findByUrl(String url);
    List<SongEntity> findAllByCreatedBy(String email);
    List<SongEntity> findAllByNameContaining(String name);
    List<SongEntity> findAllBySingers_Id(String singerId);
}
