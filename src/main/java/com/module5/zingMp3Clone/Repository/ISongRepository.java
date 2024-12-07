package com.module5.zingMp3Clone.Repository;

import com.module5.zingMp3Clone.Model.Entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISongRepository extends JpaRepository<SongEntity, String> {
    SongEntity findByUrl(String url);
    List<SongEntity> findAllByCreatedBy(String email);
    List<SongEntity> findAllByNameContaining(String name);
    List<SongEntity> findAllBySingers_Id(String singerId);
    @Modifying
    @Query("UPDATE SongEntity e SET e.numsOfListen = e.numsOfListen + 1 WHERE e.url = :url")
    void increaseListenCount(@Param("url") String url);
    @Modifying
    @Query("UPDATE SongEntity e SET e.numsOfLike = e.numsOfLike + 1 WHERE e.id = :id")
    void increaseLikeCount(@Param("id") String id);
    @Modifying
    @Query("UPDATE SongEntity e SET e.numsOfLike = e.numsOfLike - 1 WHERE e.id = :id")
    void decreaseLikeCount(@Param("id") String id);
}
