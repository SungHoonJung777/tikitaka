package org.fullstack4.tikitaka.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@MappedSuperclass
@EntityListeners(value={AuditingEntityListener.class})
public class BaseEntity {
    @CreatedDate
    @Column(name="regDate", updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDate regDate;
}
