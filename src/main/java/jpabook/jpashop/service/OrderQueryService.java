package jpabook.jpashop.service;

import jpabook.jpashop.api.OrderAPiController;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSimpleApiQuery.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllwithItem();
        List<OrderDto> all = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return all;
    }

}
