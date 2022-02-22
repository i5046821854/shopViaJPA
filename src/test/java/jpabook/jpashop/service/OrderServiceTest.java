package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        Member member = getMember();

        Book book = getBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.Order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStaus());
        assertEquals(1,getOrder.getOrderItems().size());
        assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        assertEquals( 8, book.getStockQuantity());
    }

    private Book getBook(String name, int price, int qty) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(qty);
        em.persist(book);
        return book;
    }

    private Member getMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    public void 주문취소() throws Exception{
        Member member = getMember();
        Book item = getBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.Order(member.getId(), item.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStaus());
        assertEquals( 10, item.getStockQuantity());

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception{
        Member member = getMember();
        Item item = getBook("시골 JPA", 10000, 10);

        int orderCount = 11;
        try
        {
            orderService.Order(member.getId(), item.getId(),  orderCount);
        }
        catch (NotEnoughStockException e)
        {
            return;
        }
    }

}