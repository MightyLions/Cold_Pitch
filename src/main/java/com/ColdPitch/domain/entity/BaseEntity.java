package com.ColdPitch.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @Override
    public String toString() {
        return "createAt=" + createAt +
                ", updatedAt=" + modifiedAt +
                ", createBy='" + createdBy + '\'' +
                ", updateBy='" + modifiedBy + '\'';
    }
}
