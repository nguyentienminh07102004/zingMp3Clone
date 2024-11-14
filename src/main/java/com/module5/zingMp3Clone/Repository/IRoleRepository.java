package com.module5.zingMp3Clone.Repository;

import com.module5.zingMp3Clone.Model.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<RoleEntity, String> {
}
