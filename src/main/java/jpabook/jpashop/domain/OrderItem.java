package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //빈 파라미터로 이루어진 생성자를 protected로 (createOrder만을 사용하도록 하기 위해서)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    public static OrderItem createOrderItem(Item item, int orderPRice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPRice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }
    //비즈니스 메소드

    /**
     * 취소시 재고 증가
     */
    public void cancel(){
        getItem().addStock(count);
    }

    /**
     * 주문량 * 가격으로 total price 반환
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
