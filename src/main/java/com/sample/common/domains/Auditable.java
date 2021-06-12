package com.sample.common.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sample.common.config.constants.JpaConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = { "createdAt", "createdBy", "lastUpdatedAt", "lastUpdatedBy" },
        allowGetters = true
)
public abstract class Auditable implements Serializable {
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JpaConstants.DATA_TIME_FORMAT)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = true, updatable = false)
    @CreatedBy
    private String createdBy;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_at", nullable = true)
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JpaConstants.DATA_TIME_FORMAT)
    private LocalDateTime lastUpdatedAt;

    @Column(name = "last_updated_by", nullable = true)
    @LastModifiedBy
    private String lastUpdatedBy;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
