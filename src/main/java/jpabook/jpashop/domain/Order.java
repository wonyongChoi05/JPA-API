package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @JsonIgnore
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    // 양방향 연관 관계일 때 연관 관계 편의 메소드 작성해주는 것이 좋음
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // ==생성 메서드==
    // Order 비즈니스 로직은 매우 복잡하기때문에(회원, 배송 정보, 주문 상품에 관한 로직들이 모두 필요함) 생성 메서드가 있는 것이 좋다.
    // ...문법은 한번에 여러개의 값을 넘길때 사용한다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        // 여러개의 값을 받아올 때에는 for문을 사용해야한다.
        // orderItems의 값을 모두 orderIteme에 저장한다.
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        // 주문상태는 ORDER이다.
        order.setStatus(OrderStatus.ORDER);
        // 주문시간에는 현재의 시간이 저장된다.
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    // 주문 취소
    // ==비즈니스 로직==
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 전체 주문 가격 조회
    // ==조회 로직==
    // IntelliJ에서 for -> Lamda 식으로 변경하게 지원해줌 for문 앞에 커서두고 Option + Enter
    public int getTotalPrice(){
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return totalPrice;
    }

    // public int getTotalPrice(){
    //        int totalPrice = 0;
    //        orderItems에서 상품마다의 전체 가격을 전부 가져올때까지 totalPrice에 누적시켜줌
    //        for (OrderItem orderItem : orderItems) {
    //            totalPrice += orderItem.getTotalPrice();
    //        }
    //        return totalPrice;
    //    }
}