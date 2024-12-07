package com.module5.zingMp3Clone.Repository;

import com.module5.zingMp3Clone.Model.Entity.SingerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISingerRepository extends JpaRepository<SingerEntity, String> {
    List<SingerEntity> findByNameContaining(String name);
}
