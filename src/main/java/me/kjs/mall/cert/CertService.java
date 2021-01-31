package me.kjs.mall.cert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.cert.dto.CertTokenDto;
import me.kjs.mall.cert.exception.NotAvailableCertificationException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.member.exception.NoExistPhoneNumberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CertService {

    private final CertRepository certRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public CertTokenDto generatorPasswordModifyCertKey(Member member) {

        Cert cert = Cert.createMemberCert(CertType.MEMBER_PASSWORD_MODIFY_TOKEN, member);
        Cert saveCert = certRepository.save(cert);

        return CertTokenDto.certToTokenDto(saveCert);
    }

    @Transactional
    public CertTokenDto generatorPhoneNumberCertKey(String phoneNumber) {
        Cert cert = certRepository.save(Cert.createPhoneNumberCert(phoneNumber));
        return CertTokenDto.certToTokenDto(cert);
    }

    @Transactional
    public CertTokenDto generatorPhoneNumberCertKey(Member member) {
        Cert cert = certRepository.save(Cert.createPhoneNumberCert(member));
        return CertTokenDto.certToTokenDto(cert);
    }

    @Transactional
    public CertTokenDto generatorRegisterCertKey(String phoneNumber) {
        Cert cert = certRepository.save(Cert.createRegisterCert(phoneNumber));
        return CertTokenDto.certToTokenDto(cert);
    }

    @Transactional
    public CertTokenDto generatorFindEmailCertKey(String phoneNumber) {
        Member member = memberRepository.findByPhoneNumber(phoneNumber).orElseThrow(NoExistPhoneNumberException::new);
        Cert cert = certRepository.save(Cert.createEmailCheckCert(member));
        return CertTokenDto.certToTokenDto(cert);
    }

    @Transactional
    public Member checkCertPhoneKey(String phoneNumber, String certKey) {
        Cert cert = certRepository.findTopByCertTypeAndTokenAndExpiredDateTimeAfter(CertType.PHONE_CERT_TOKEN, certKey, LocalDateTime.now()).orElseThrow(NotAvailableCertificationException::new);
        if (!cert.isEqualKey(phoneNumber)) {
            throw new NotAvailableCertificationException();
        }
        cert.expireCert();
        return cert.getMember();
    }

    @Transactional
    public void checkCertJoinKey(String phoneNumber, String certKey) {
        Cert cert = certRepository.findTopByCertTypeAndTokenAndExpiredDateTimeAfter(CertType.MEMBER_REGISTER_TOKEN, certKey, LocalDateTime.now()).orElseThrow(NotAvailableCertificationException::new);
        if (!cert.isEqualKey(phoneNumber)) {
            throw new NotAvailableCertificationException();
        }
        cert.expireCert();
    }

    @Transactional
    public String checkCertEmailFindKey(String certKey) {
        Cert cert = certRepository.findTopByCertTypeAndTokenAndExpiredDateTimeAfter(CertType.EMAIL_FIND_TOKEN, certKey, LocalDateTime.now()).orElseThrow(NotAvailableCertificationException::new);
        cert.expireCert();
        return cert.getMember().getEmail();
    }

    @Transactional
    public Member checkCertPasswordModify(String certKey) {
        Cert cert = certRepository.findTopByCertTypeAndTokenAndExpiredDateTimeAfter(CertType.MEMBER_PASSWORD_MODIFY_TOKEN, certKey, LocalDateTime.now()).orElseThrow(NotAvailableCertificationException::new);
        cert.expireCert();
        return cert.getMember();
    }
}
