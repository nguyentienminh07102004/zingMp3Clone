package com.module5.zingMp3Clone.Repository;

import com.module5.zingMp3Clone.Model.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, String> {
}
