package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.Getter;

@Data
public class OrderSearch {

    public String memberName;
    public OrderStatus orderStatus;
}
