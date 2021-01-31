package me.kjs.mall.destination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.ThrowUtil;
import me.kjs.mall.destination.dto.DestinationSaveDto;
import me.kjs.mall.destination.exception.DestinationLimitOverException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DestinationService {

    private final MemberRepository memberRepository;

    private final DestinationRepository destinationRepository;

    @Transactional
    public Destination createDestination(DestinationSaveDto destinationSaveDto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        if (member.isAvailableAddDestination() == false) {
            throw new DestinationLimitOverException();
        }
        Destination destination = Destination.createDestination(destinationSaveDto, member);

        Destination saveDestination = destinationRepository.save(destination);
        member.addDestination(saveDestination);
        return saveDestination;
    }

    @Transactional
    public void removeDestination(Long memberId, Long destinationId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        Destination destination = destinationRepository.findById(destinationId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notOwnerThrow(destination, member);
        destinationRepository.delete(destination);
    }

    @Transactional
    public void updateDestination(Long memberId, Long destinationId, DestinationSaveDto destinationSaveDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        Destination destination = destinationRepository.findById(destinationId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notOwnerThrow(destination, member);
        destination.update(destinationSaveDto);
    }

    public List<Destination> findDestinationByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);

        return destinationRepository.findByMemberOrderByIdDesc(member);
    }

}
