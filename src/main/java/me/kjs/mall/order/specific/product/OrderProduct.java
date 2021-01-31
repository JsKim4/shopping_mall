package me.kjs.mall.order.specific.product;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.OwnerCheck;
import me.kjs.mall.common.util.CodeGeneratorUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.dto.create.ProductAndQuantityDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.OrderDestination;
import me.kjs.mall.order.specific.exchange.OrderExchange;
import me.kjs.mall.order.specific.exchange.OrderExchangeProduct;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.payment.exception.PaymentProductStockNotEnoughException;
import me.kjs.mall.point.Point;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductStockModifyDto;
import me.kjs.mall.review.Review;
import me.kjs.mall.review.dto.ReviewCreateDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderProduct extends BaseEntity implements OwnerCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String orderItemCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_specific_id")
    private OrderSpecific orderSpecific;
    @Embedded
    private OrderProductPayment orderProductPayment;
    @Enumerated(EnumType.STRING)
    private OrderState orderProductState;

    @Transient
    @Enumerated(EnumType.STRING)
    private OrderState preOrderProductState;

    @Enumerated(EnumType.STRING)
    private OrderExchangeState orderProductExchangeState;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    private Review review;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_exchange_product_id")
    private OrderExchangeProduct orderExchangeProduct;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "orderProduct")
    @Builder.Default
    private List<OrderProductLog> orderProductLog = new ArrayList<>();

    public static OrderProduct createOrderProduct(OrderSpecific orderSpecific, ProductAndQuantityDto productDto, int accumulateExpectedPoint) {
        OrderProduct orderProduct = OrderProduct.builder()
                .product(productDto.getProduct())
                .orderSpecific(orderSpecific)
                .orderItemCode(CodeGeneratorUtil.orderItemCodeGenerate())
                .orderProductPayment(OrderProductPayment.createOrderProductPayment(productDto, accumulateExpectedPoint))
                .orderProductExchangeState(OrderExchangeState.NONE)
                .orderExchangeProduct(null)
                .build();
        orderProduct.modifyOrderState(OrderState.ORDER_CREATE);
        return orderProduct;
    }

    public boolean isAvailableExchangeRequest(int quantity) {
        return !orderProductPayment.isRemainQuantityLessThen(quantity);
    }

    public int getSumOriginPrice() {
        return orderProductPayment.getSumOriginPrice();
    }

    public int getSumDiscountPrice() {
        return orderProductPayment.getSumDiscountPrice();
    }

    public int getSumPrice() {
        return orderProductPayment.getSumPrice();
    }

    public int calculateDeliveryFee(int sumPrice) {
        return product.calculateDeliveryFee(sumPrice);
    }

    public String getProductName() {
        return product.getBaseProductName();
    }

    public int getQuantity() {
        return orderProductPayment.getQuantity();
    }

    public void paymentApproveSuccess() {
        modifyOrderState(OrderState.PAYMENT_WAIT);
    }

    public void paymentAccept() {
        modifyOrderState(OrderState.PAYMENT_ACCEPT);
    }

    public void paymentApproveFail() {
        product.stockModify(ProductStockModifyDto.paymentApproveFail(orderProductPayment.getQuantity()));
    }

    public void paymentApproveRequest() {
        if (product.isStockMoreThen(orderProductPayment.getQuantity()) == false) {
            throw new PaymentProductStockNotEnoughException();
        }
        product.stockModify(ProductStockModifyDto.paymentApproveRequest(orderProductPayment.getQuantity() * (-1)));
    }

    public OrderProduct loading() {
        product.loading();
        if (orderExchangeProduct != null) {
            orderExchangeProduct.getQuantity();
        }
        return this;
    }

    public void orderCancel() {
        modifyOrderState(OrderState.PAYMENT_CANCEL);
        product.stockModify(ProductStockModifyDto.orderCancel(orderProductPayment.getQuantity()));
    }

    public void orderAccept() {
        orderProductState = OrderState.ORDER_ACCEPT;
    }

    public boolean isAvailableOrderAcceptAvailable() {
        return orderProductState == OrderState.DELIVERY_ACCEPT &&
                (orderProductExchangeState == OrderExchangeState.NONE ||
                        orderProductExchangeState == OrderExchangeState.EXCHANGE_ACCEPT);
    }

    public void modifyOrderState(OrderState orderState) {
        this.orderProductState = orderState;
        addLog();
    }

    public void deliveryDoingOrder() {
        modifyOrderState(OrderState.DELIVERY_DOING);
    }

    public void deliveryAcceptOrder() {
        modifyOrderState(OrderState.DELIVERY_ACCEPT);
    }

    public void requestExchange(int quantity, OrderExchange orderExchange) {
        orderProductExchangeState = OrderExchangeState.EXCHANGE_REQUEST;
        orderExchangeProduct = OrderExchangeProduct.requestExchange(this, quantity, orderExchange);
    }

    public void modifyOrderExchangeState(OrderExchangeState orderExchangeState) {
        orderProductExchangeState = orderExchangeState;
    }

    public int getExchangeQuantity() {
        if (orderExchangeProduct == null || orderProductExchangeState == OrderExchangeState.NONE) {
            return 0;
        }
        return orderExchangeProduct.getQuantity();
    }

    public void cancelExchange() {
        if (orderExchangeProduct != null) {
            orderExchangeProduct.remove();
            orderExchangeProduct = null;
        }
        orderProductExchangeState = OrderExchangeState.NONE;
    }

    @Override
    public boolean isOwner(Member member) {
        return orderSpecific.isOwner(member);
    }

    public Review createReview(ReviewCreateDto reviewCreateDto, Member member) {
        review = Review.createReview(reviewCreateDto, this, member);
        product.addReview(reviewCreateDto.getScore());
        return review;
    }

    public boolean isAvailableReview() {
        return orderProductState == OrderState.ORDER_ACCEPT;
    }

    public Long getOrderProductId() {
        return id;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Long getMemberId() {
        return orderSpecific.getMemberId();
    }

    public Long getOrderSpecificId() {
        return orderSpecific.getId();
    }

    public Long getOrderId() {
        return orderSpecific.getOrderId();
    }


    public String getOrderCode() {
        return orderSpecific.getOrderCode();
    }

    public OrderState getOrderProductState() {
        return orderProductState;
    }

    public String getProductCode() {
        return product.getProductCode();
    }

    public String getMemberName() {
        return orderSpecific.getMemberName();
    }

    public String getMemberEmail() {
        return orderSpecific.getMemberEmail();
    }

    public String getRecipient() {
        return orderSpecific.getMemberRecipient();
    }

    public int getAccumulateExpectedPoint() {
        return orderProductPayment.getAccumulateExpectedPoint();
    }

    public Order getOrder() {
        return orderSpecific.getOrder();
    }

    public void addLog() {
        this.orderProductLog.add(OrderProductLog.createOrderProductLog(this));
    }


    public OrderDestination getOrderDestination() {
        return orderSpecific.getOrderDestination();
    }

    public boolean isAvailablePlaceOrder() {
        return orderProductState == OrderState.PAYMENT_ACCEPT || orderProductState == OrderState.PLACE_ORDER_WAIT;
    }

    public void placeAccept() {
        modifyOrderState(OrderState.DELIVERY_WAIT);
    }

    public void accumulatePoint(Point point) {
        orderProductPayment.setAccumulatePoint(point);
    }

    public void waitCancel() {
        modifyOrderState(OrderState.ORDER_CREATE);
    }


    public int getOrderSumPrice() {
        return orderSpecific.getSumPrice();
    }

    public LocalDate getPaymentAcceptDate() {
        LocalDateTime paymentDateTime = orderSpecific.getPaymentDateTime();
        if (paymentDateTime != null)
            return paymentDateTime.toLocalDate();
        return null;
    }

    public int getUsePoint() {
        return orderSpecific.getUsePoint();
    }
}