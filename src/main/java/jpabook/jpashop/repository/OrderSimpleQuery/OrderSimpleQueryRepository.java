package jpabook.jpashop.repository.OrderSimpleQuery;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

//리포지토리에서는 순수한 엔티티만 조회하는게 낫다 >> DTO조회는 따로 만들어놓는게 나음
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;


    public List<SimpleOrderQueryDto> findOrderDto() {
        return em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " join o.member m" +
                " join o.delivery d", SimpleOrderQueryDto.class).getResultList();   //select는 엔티티나 임베더블만 가져올 수 있으므로 DTO는 저렇게 new로 select해줘야함
    }

}
