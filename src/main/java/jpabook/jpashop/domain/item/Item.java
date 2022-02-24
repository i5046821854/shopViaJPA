package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@BatchSize(size = 100)
@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  //상속 시 한 테이블에 모두 몰아 넣음
@DiscriminatorColumn(name = "dtype") //각 테이블 구분컬럼
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    /**
     *  엔티티가 가지고 있는 필드에 대한 조작을 꾀하는 비즈니스 로직은 해당 엔티티에서 정의해주는 것이 좋음
     *  service에서 setter로 쓰는 것이 아니라!
     **/
    //재고 증가
    public void addStock(int qty){
        this.stockQuantity += qty;
    }

    //재고 감소
    public void removeStock(int qty){
        int restStk = this.stockQuantity - qty;
        if(restStk < 0)
        {
            throw new NotEnoughStockException("need more stock");  //exception
        }
        setStockQuantity(restStk);
    }

    public void changeItem(int price, String name, int qty)
    {
        setPrice(price);
        setName(name);
        setStockQuantity(qty);

    }

}
