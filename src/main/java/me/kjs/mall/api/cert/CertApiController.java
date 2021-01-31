package me.kjs.mall.api.cert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.cert.CertService;
import me.kjs.mall.cert.dto.CertTokenDto;
import me.kjs.mall.cert.exception.NoExistMemberException;
import me.kjs.mall.common.dto.EmailOnlyDto;
import me.kjs.mall.common.dto.EmailPhoneNumberDto;
import me.kjs.mall.common.dto.PhoneNumberCertDto;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.connect.ConnectService;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.member.exception.AlreadyExistPhoneNumberException;
import me.kjs.mall.member.exception.NoExistPhoneNumberException;
import me.kjs.mall.member.exception.join.AlreadyExistEmailException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/certs")
public class CertApiController {

    private final CertService certService;

    private final ConnectService connectService;

    private final MemberRepository memberRepository;

    @PostMapping("/email/phone")
    public ResponseDto generatorEmailPhoneCert(@RequestBody @Validated PhoneNumberCertDto phoneNumberCertDto, Errors errors) {
        hasErrorsThrow(errors);

        Member member = memberRepository.findByPhoneNumber(phoneNumberCertDto.getPhoneNumber()).orElseThrow(NoExistPhoneNumberException::new);

        CertTokenDto certTokenDto = certService.generatorPhoneNumberCertKey(member);

        connectService.sendPhoneCertSms(phoneNumberCertDto.getPhoneNumber(), certTokenDto.getToken());
        return ResponseDto.created(null);
    }

    @PostMapping("/password/phone")
    public ResponseDto generatorPasswordPhoneCert(@RequestBody @Validated EmailPhoneNumberDto emailPhoneNumberDto, Errors errors) {
        hasErrorsThrow(errors);

        Member member = memberRepository.findByPhoneNumber(emailPhoneNumberDto.getPhoneNumber()).orElseThrow(NoExistMemberException::new);
        if (!member.getEmail().equals(emailPhoneNumberDto.getEmail())) {
            throw new NoExistMemberException();
        }
        CertTokenDto certTokenDto = certService.generatorPhoneNumberCertKey(member);

        connectService.sendPhoneCertSms(emailPhoneNumberDto.getPhoneNumber(), certTokenDto.getToken());
        return ResponseDto.created(null);
    }

    @PostMapping("/email/check/phone")
    public ResponseDto emailCheckPhoneCert(@RequestBody @Validated PhoneNumberCertDto phoneNumberCertDto, Errors errors) {
        hasErrorsThrow(errors);
        certService.checkCertPhoneKey(phoneNumberCertDto.getPhoneNumber(), phoneNumberCertDto.getToken());

        CertTokenDto certTokenDto = certService.generatorFindEmailCertKey(phoneNumberCertDto.getPhoneNumber());
        return ResponseDto.created(certTokenDto);
    }

    @PostMapping("/password/check/phone")
    public ResponseDto passwordCheckPhoneCert(@RequestBody @Validated PhoneNumberCertDto phoneNumberCertDto, Errors errors) {
        hasErrorsThrow(errors);
        Member member = certService.checkCertPhoneKey(phoneNumberCertDto.getPhoneNumber(), phoneNumberCertDto.getToken());

        CertTokenDto certTokenDto = certService.generatorPasswordModifyCertKey(member);
        return ResponseDto.created(certTokenDto);
    }


    @PostMapping("/join/phone")
    public ResponseDto generatorPhoneCert(@RequestBody @Validated PhoneNumberCertDto phoneNumberCertDto, Errors errors) {
        hasErrorsThrow(errors);
        if (memberRepository.existsByPhoneNumber(phoneNumberCertDto.getPhoneNumber())) {
            throw new AlreadyExistPhoneNumberException();
        }
        CertTokenDto certTokenDto = certService.generatorPhoneNumberCertKey(phoneNumberCertDto.getPhoneNumber());

        connectService.sendPhoneCertSms(phoneNumberCertDto.getPhoneNumber(), certTokenDto.getToken());
        return ResponseDto.created(null);
    }

    @PostMapping("/check/email")
    public ResponseDto checkEmail(@RequestBody @Validated EmailOnlyDto emailOnlyDto, Errors errors) {
        if (memberRepository.existsByEmail(emailOnlyDto.getEmail())) {
            throw new AlreadyExistEmailException();
        }
        return ResponseDto.created(null);
    }

    @PostMapping("/check/phone")
    public ResponseDto checkPhoneCert(@RequestBody @Validated PhoneNumberCertDto phoneNumberCertDto, Errors errors) {
        hasErrorsThrow(errors);
        certService.checkCertPhoneKey(phoneNumberCertDto.getPhoneNumber(), phoneNumberCertDto.getToken());

        CertTokenDto certTokenDto = certService.generatorRegisterCertKey(phoneNumberCertDto.getPhoneNumber());
        return ResponseDto.created(certTokenDto);
    }

}
