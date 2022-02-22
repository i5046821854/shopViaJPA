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
                em.merge(item);  //업데이트트
           }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
