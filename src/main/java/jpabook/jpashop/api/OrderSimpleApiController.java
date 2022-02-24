package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQuery.OrderSimpleQueryRepository;
import jpabook.jpashop.repository.OrderSimpleQuery.SimpleOrderQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//다대일 / 일대일 관계에서 성능 최적화
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private  final OrderRepository orderRepository;
    private  final OrderSimpleQueryRepository orderSimpleQueryRepository;


    @GetMapping("/api/v1/simple-orders")  //이는 문제임. 엔티티를  api로 넘기면 안되고,
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAll(new OrderSearch());  //양방향 매핑시 한쪽에 @jsonIgnore해줘야함 + hibernate5module로 지연 로딩된 객체 null값으로 변경
        for(Order order : all){  //원하는 컬럼만
            order.getMember().getName(); //Lazy(지연로딩)가 강제 초기화
            order.getDelivery().getAddress(); //Lazy(지연로딩)가 강제 초기화됨
        }
        return all;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //가능하면 V2 / V3으로 해결됨 //

    @GetMapping("/api/v2/simple-orders")  //사실 이거는 Result와 같은 클래스로 싸서 반환하는게 좋음
    public List<SimpleOrderDto> ordersV2(){ //DTO로 바꾸었지만 조인된 컬럼에 대한 N+1문제 여전히 존재
        List<Order> orders = orderRepository.findAll(new OrderSearch());  //양방향 매핑시 한쪽에 @jsonIgnore해줘야함 + hibernate5module로 지연 로딩된 객체 null값으로 변경
        List<SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3/simple-orders")  //fetch join으로 조인된 컬럼도 한번에
    public List<SimpleOrderDto> ordersV3(){
        List<Order>orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
        return result;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDto();  //엔티티를 안거치고 DTO로 바로, 필요한 컬럼만 / 빠른데 DTO로 딱 고정된 값만 가져오므로 재사용성이 떨어짐 / V3과 V4 사이 trade-off을 선택해야함 / 성능 차이가 그리 크지는 않음
   }



    @Data
    static class SimpleOrderDto{
        private String name;
        private Long orderId;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();  //lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //lazy 초기화
        }

    }

}
