package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
            if(item.getId() == null)  //처음 저장할때는 id가 없음 (generatedValue로 자동 생성 되기 전이기 때문)
            {
                em.persist(item);
            }
            else  //이미 있는 것 (영속성 컨텍스트에 있음)
            {
                em.merge(item);  //준영속 -> 영속으로 / 하지만 모든 필드를 다 교체하므로 값이 없는 경우는 null로 변경될 위험 / 항상 이거말고 변경감지 (itemService)를 사용하라
           }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
