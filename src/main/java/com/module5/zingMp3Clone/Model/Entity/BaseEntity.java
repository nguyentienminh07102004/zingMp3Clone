package com.module5.zingMp3Clone.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
@Getter
@Setter
public class BaseEntity {
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate;
    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDate modifiedDate;
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;
}
