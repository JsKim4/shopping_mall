package me.kjs.mall.cert;

import lombok.*;
import me.kjs.mall.common.BaseEntityDateTime;
import me.kjs.mall.member.Member;

import javax.persistence.*;
import java.time.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cert extends BaseEntityDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_cert_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CertType certType;

    private String token;

    private LocalDateTime expiredDateTime;

    private String keyText;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Cert createEmailCheckCert(Member member) {
        CertType certType = CertType.EMAIL_FIND_TOKEN;
        return Cert.builder()
                .certType(certType)
                .token(certType.generatorKey())
                .expiredDateTime(certType.getExpiredDateTime())
                .member(member)
                .build();
    }

    public static Cert createPhoneNumberCert(String phoneNumber) {
        CertType certType = CertType.PHONE_CERT_TOKEN;
        return Cert.builder()
                .certType(certType)
                .token(certType.generatorKey())
                .expiredDateTime(certType.getExpiredDateTime())
                .keyText(phoneNumber)
                .build();
    }

    public Member getMember() {
        return member;
    }

    public static Cert createMemberCert(CertType certType, Member member) {
        return Cert.builder()
                .certType(certType)
                .token(certType.generatorKey())
                .expiredDateTime(certType.getExpiredDateTime())
                .member(member)
                .build();
    }

    public static Cert createPhoneNumberCert(Member member) {
        CertType certType = CertType.PHONE_CERT_TOKEN;
        return Cert.builder()
                .certType(certType)
                .token(certType.generatorKey())
                .expiredDateTime(certType.getExpiredDateTime())
                .member(member)
                .build();
    }

    public static Cert createRegisterCert(String key) {
        CertType certType = CertType.MEMBER_REGISTER_TOKEN;
        return Cert.builder()
                .certType(certType)
                .token(certType.generatorKey())
                .expiredDateTime(certType.getExpiredDateTime())
                .keyText(key)
                .build();
    }

    public String getToken() {
        return token;
    }

    public Long getRemainExpiredDateTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate standDate = now.toLocalDate();
        LocalTime standTime = now.toLocalTime();
        LocalTime expireTime = expiredDateTime.toLocalTime();
        LocalDate expireDate = expiredDateTime.toLocalDate();
        Period period = Period.between(standDate, expireDate);
        Duration duration = Duration.between(standTime, expireTime);
        return period.getDays() * 24 * 60 * 60 + duration.getSeconds();
    }

    public void expireCert() {
        expiredDateTime = LocalDateTime.now();
    }

    public boolean isEqualKey(String phoneNumber) {
        if (phoneNumber == null)
            return false;
        String text = keyText == null ? member.getPhoneNumber() : keyText;
        return phoneNumber.equals(text);
    }
}
