package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {


    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item"
            , joinColumns = @JoinColumn(name= "category_id")
            , inverseJoinColumns = @JoinColumn(name = "item_id"))
    //다대다는 컬렉션 관계를 양쪽에 가질 수 없으므로, 중간 테이블을 만들어야함/ 실무에서는 쓰지 x (여러 가지 컬럼을 추가할 수 없음)
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)   //부모 카테고리 조회
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")  //자식 카테고리 조회
    private List<Category> child = new ArrayList<>();   //컬렉션은 이렇게 필드에서 초기화하는 것이 좋다

    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);

    }
}
