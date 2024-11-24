package com.module5.zingMp3Clone.Repository;

import com.module5.zingMp3Clone.Model.Entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISongRepository extends JpaRepository<SongEntity, String> {
}
