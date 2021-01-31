package me.kjs.mall.destination;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.OwnerCheck;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.destination.dto.DestinationSaveDto;
import me.kjs.mall.member.Member;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Destination extends BaseEntity implements OwnerCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String name;
    private String recipient;
    private String tel1;
    private String tel2;
    private String addressSimple;
    private String addressDetail;
    private String zipcode;

    private YnType defaultDestinationYn;

    public static Destination createDestination(DestinationSaveDto destinationSaveDto, Member member) {
        return Destination.builder()
                .member(member)
                .name(destinationSaveDto.getDestinationName())
                .recipient(destinationSaveDto.getRecipient())
                .tel1(destinationSaveDto.getTel1())
                .tel2(destinationSaveDto.getTel2())
                .addressSimple(destinationSaveDto.getAddressSimple())
                .addressDetail(destinationSaveDto.getAddressDetail())
                .zipcode(destinationSaveDto.getZipcode())
                .defaultDestinationYn(YnType.N)
                .build();
    }

    @Override
    public boolean isOwner(Member member) {
        return this.member == member;
    }

    public void update(DestinationSaveDto destinationSaveDto) {
        if (CollectionTextUtil.isNotBlank(destinationSaveDto.getDestinationName())) {
            name = destinationSaveDto.getDestinationName();
        }
        if (CollectionTextUtil.isNotBlank(destinationSaveDto.getRecipient())) {
            recipient = destinationSaveDto.getRecipient();
        }
        if (CollectionTextUtil.isNotBlank(destinationSaveDto.getTel1())) {
            tel1 = destinationSaveDto.getTel1();
        }
        tel2 = destinationSaveDto.getTel2();
        tel2 = CollectionTextUtil.isBlank(tel2) ? null : tel2;
        if (CollectionTextUtil.isNotBlank(destinationSaveDto.getAddressSimple())) {
            addressSimple = destinationSaveDto.getAddressSimple();
        }
        if (CollectionTextUtil.isNotBlank(destinationSaveDto.getAddressDetail())) {
            addressDetail = destinationSaveDto.getAddressDetail();
        }
        if (CollectionTextUtil.isNotBlank(destinationSaveDto.getZipcode())) {
            zipcode = destinationSaveDto.getZipcode();
        }
    }

    public String getTel2() {
        if (tel2 == null)
            return "";
        return tel2;
    }
}

