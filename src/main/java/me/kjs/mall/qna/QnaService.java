package me.kjs.mall.qna;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonSlice;
import me.kjs.mall.common.dto.OnlyContentDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductRepository;
import me.kjs.mall.qna.dto.QnaCreateDto;
import me.kjs.mall.qna.dto.QnaSearchCondition;
import me.kjs.mall.qna.dto.QnaUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static me.kjs.mall.common.util.AvailableUtil.hasRole;
import static me.kjs.mall.common.util.ThrowUtil.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class QnaService {
    private final QnaRepository qnaRepository;
    private final ProductRepository productRepository;
    private final QnaQueryRepository qnaQueryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Qna createQna(QnaCreateDto qnaCreateDto, Long productId, Member member) {
        Product product = productRepository.findById(productId).orElseThrow(NoExistIdException::new);
        notUsedThrow(product);
        product.loading();
        Qna qna = Qna.qnaCreate(qnaCreateDto, product, member);
        return qnaRepository.save(qna);
    }

    public CommonSlice<Qna> findQnaBySearchCondition(QnaSearchCondition qnaSearchCondition, Member member) {
        Product product = null;
        Member mergeMember = member == null ? null : memberRepository.findById(member.getId()).orElseThrow(NoExistIdException::new);
        if (qnaSearchCondition.getProductId() != null) {
            product = productRepository.findById(qnaSearchCondition.getProductId()).orElseThrow(NoExistIdException::new);
            notUsedThrow(product);
        }
        CommonSlice<Qna> qnaCommonSlice = qnaQueryRepository.findQnaBySearchCondition(product, qnaSearchCondition, member);

        if (!hasRole(mergeMember, AccountRole.QNA)) {
            for (Qna content : qnaCommonSlice.getContents()) {
                content.checkMine(mergeMember);
            }
        }
        for (Qna content : qnaCommonSlice.getContents()) {
            content.loading();
        }
        return qnaCommonSlice;
    }

    @Transactional
    public void updateQna(QnaUpdateDto qnaUpdateDto, Long qnaId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(qna);
        notOwnerThrow(qna, member);
        qna.update(qnaUpdateDto);
    }

    @Transactional
    public void deleteQna(Long qnaId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(qna);
        notOwnerThrow(qna, member);
        qna.delete();
    }

    @Transactional
    public void updateQnaAnswer(Long qnaId, OnlyContentDto onlyContentDto) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(qna);
        qna.updateAnswer(onlyContentDto.getContent());
    }
}
