package me.kjs.mall.common;


import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.Member;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@MappedSuperclass
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity extends BaseEntityDateTime {

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "createdBy")
    private Member createdBy;
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "lastModifiedBy")
    private Member lastModifiedBy;

}
