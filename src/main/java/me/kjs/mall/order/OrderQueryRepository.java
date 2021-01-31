package me.kjs.mall.order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.QMember;
import me.kjs.mall.order.cancel.OrderCancel;
import me.kjs.mall.order.cancel.QOrderCancel;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.dto.OrderAdminSearchCondition;
import me.kjs.mall.order.dto.OrderSearchConditionDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.QOrderSpecific;
import me.kjs.mall.order.specific.destination.QOrderDestination;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.order.specific.product.QOrderProduct;
import me.kjs.mall.product.QProduct;
import me.kjs.mall.product.base.QBaseProduct;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderQueryRepository {
    private final EntityManager em;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public List<OrderSpecific> findOrderByMemberAndCondition(OrderSearchConditionDto orderSearchConditionDto, Member member) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QOrderSpecific.orderSpecific.order.orderMember.eq(member));
        booleanBuilder.and(QOrderSpecific.orderSpecific.orderState.notIn(OrderState.ORDER_CREATE));
        if (orderSearchConditionDto.getOrderState() != null) {
            booleanBuilder.and(QOrderSpecific.orderSpecific.orderState.eq(orderSearchConditionDto.getOrderState()));
        } else {
            booleanBuilder.and(QOrderSpecific.orderSpecific.orderState.notIn(OrderState.ORDER_CREATE));
        }

        List<OrderSpecific> orderSpecificQueryResults = queryFactory.select(QOrderSpecific.orderSpecific)
                .from(QOrderSpecific.orderSpecific)
                .join(QOrderSpecific.orderSpecific.order, QOrder.order)
                .join(QOrder.order.orderMember, QMember.member)
                .where(booleanBuilder)
                .orderBy(QOrderSpecific.orderSpecific.createdDate.desc())
                .limit(orderSearchConditionDto.getLimit())
                .offset(orderSearchConditionDto.getOffset())
                .fetch();
        return orderSpecificQueryResults;
    }

    public OrderCancel findOrderCancelById(Long id) {
        return queryFactory.selectFrom(QOrderCancel.orderCancel)
                .where(QOrderCancel.orderCancel.id.eq(id))
                .fetchFirst();
    }


    public CommonPage<OrderProduct> findOrderProductByAdminSearchCondition(OrderAdminSearchCondition orderAdminSearchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (CollectionTextUtil.isNotBlank(orderAdminSearchCondition.getOrderState())) {
            booleanBuilder.and(QOrderProduct.orderProduct.orderProductState.in(orderAdminSearchCondition.getOrderState()));
        } else {
            booleanBuilder.and(QOrderProduct.orderProduct.orderProductState.notIn(OrderState.ORDER_CREATE));
        }
        if (orderAdminSearchCondition.getOrderEndDate() != null) {
            booleanBuilder.and(QOrderProduct.orderProduct.createdDate.before(orderAdminSearchCondition.getOrderEndDate().plusDays(1).atTime(0, 0)));
        }
        if (orderAdminSearchCondition.getOrderBeginDate() != null) {
            booleanBuilder.and(QOrderProduct.orderProduct.createdDate.after(orderAdminSearchCondition.getOrderBeginDate().atTime(0, 0)));
        }
        QueryResults<OrderProduct> results = queryFactory.selectFrom(QOrderProduct.orderProduct)
                .leftJoin(QOrderProduct.orderProduct.product, QProduct.product)
                .fetchJoin()
                .leftJoin(QProduct.product.baseProduct, QBaseProduct.baseProduct)
                .fetchJoin()
                .leftJoin(QOrderProduct.orderProduct.orderSpecific, QOrderSpecific.orderSpecific)
                .fetchJoin()
                .leftJoin(QOrderSpecific.orderSpecific.order, QOrder.order)
                .fetchJoin()
                .leftJoin(QOrderSpecific.orderSpecific.orderDestination, QOrderDestination.orderDestination)
                .fetchJoin()
                .leftJoin(QOrder.order.orderMember, QMember.member)
                .fetchJoin()
                .leftJoin(QOrder.order.nonMember, QNonMember.nonMember)
                .fetchJoin()
                .where(booleanBuilder)
                .orderBy(QOrderProduct.orderProduct.createdDate.desc())
                .limit(orderAdminSearchCondition.getLimit())
                .offset(orderAdminSearchCondition.getOffset())
                .fetchResults();
        return new CommonPage<>(results, orderAdminSearchCondition.getPage());

    }


    public int findOrderPriceByMonth(LocalDateTime now) {
        List<OrderState> orderStates = OrderState.getOrderStateOnAggregatePrice();

        Integer sumPrice = queryFactory.select(QOrderSpecific.orderSpecific.orderSpecificPayment.sumPrice.sum())
                .from(QOrderSpecific.orderSpecific)
                .where(QOrderSpecific.orderSpecific.orderState.in(orderStates).and(
                        QOrderSpecific.orderSpecific.orderDateTime.month().eq(now.getMonthValue()).and(
                                QOrderSpecific.orderSpecific.orderDateTime.year().eq(now.getYear()))))
                .fetchFirst();
        if (sumPrice == null)
            return 0;
        return sumPrice;
    }
}
