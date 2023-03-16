package kr.co.bookand.backend.common.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedDate
    protected String createdAt;
    @LastModifiedDate
    protected String modifiedAt;
    protected boolean visibility = true;

    @PrePersist
    protected void onPrePersist(){
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.modifiedAt = this.createdAt;
    }

    @PreUpdate
    protected void onPreUpdate(){
        this.modifiedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public void softDelete() {
        this.visibility = false;
    }

    public void setVisible() {
        visibility = true;
    }

}
