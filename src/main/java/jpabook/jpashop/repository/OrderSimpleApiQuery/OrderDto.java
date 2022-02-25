package jpabook.jpashop.repository.OrderSimpleApiQuery;

import jpabook.jpashop.api.OrderAPiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDto{
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private jpabook.jpashop.domain.OrderStatus OrderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;   //얘도 엔티티니까 노출되면 안되고 DTO로 반환해서 줘야함

    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        OrderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
        order.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem)).collect(Collectors.toList());
    }
}