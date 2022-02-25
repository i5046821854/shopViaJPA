package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleApiQuery.OrderFlatDto;
import jpabook.jpashop.repository.OrderSimpleApiQuery.OrderItemQueryDto;
import jpabook.jpashop.repository.OrderSimpleApiQuery.OrderQueryDto;
import jpabook.jpashop.repository.OrderSimpleApiQuery.OrderQueryRepository;
import jpabook.jpashop.service.OrderQueryService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@RestController
@RequiredArgsConstructor
public class OrderAPiController {

    //일대다 컬렉션 조회

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders") //엔티티를  api로 넘기면 안됨
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")   //DTO로 넘기지만 안에 있는 콜렉션도 엔티티이므로 이를 DTO로 넘김, 하지만 뻥튀기 데이터들에 대해 쿼리 낭비
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<OrderDto> all = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return all;
    }

    private final OrderQueryService orderQueryService;

    @GetMapping("/api/v3/orders")   //fetch join으로 해결 / 하지만 fetch join은 페이징 불가하다는 단점이 (일대다 일때만)
    public List<OrderDto> ordersV3(){
        //return orderQueryService.ordersV3(); osiv가 꺼져있다면, 한 트랜잭션 안에서 모든 데이터 처리가 끝나야하므로 다른 서비스를 만들어서 그 서비스의 트랜젝션에서 이를 처리하도록 해야한다.

        List<Order> orders = orderRepository.findAllwithItem();
        List<OrderDto> all = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return all;
    }

    @GetMapping("/api/v3.1/orders")   //batchSize를 이용하여 일대다 페이징 해결
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0" ) int offset,
                                        @RequestParam(value = "limit", defaultValue = "100" ) int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> all = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return all;
    }

    @GetMapping("/api/v4/orders")   //쿼리에서 DTO로 가져오기 / 하지만 1+n 문제
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")   //1+n 문제를 in쿼리로 해결, ToOne한번 하고 ToMany로 컬렉션 조회
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_Optimization();
    }

/*
    @GetMapping("/api/v6/orders")   //한 쿼리에 다 때려박아서 조회 가능 , 하지만 뻥튀기 존재
    public List<OrderQueryDto> ordersV6(){
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
        return flats.stream()   //orderFlatDto를 orderQueryDto로 변환
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }
*/



    @Data
    static class OrderDto{
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

    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }


}
