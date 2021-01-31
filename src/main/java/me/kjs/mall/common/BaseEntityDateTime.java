package me.kjs.mall.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityDateTime {
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    public LocalDate getLastModifiedByDateOnlyDate() {
        if (lastModifiedDate == null) {
            return LocalDate.now();
        }
        return lastModifiedDate.toLocalDate();
    }
}
