package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext /@RequiredArgsConstructor 덕분에 자동 바인딩 (생성자 injection임)
    private final EntityManager em;  //entitiyManager 자동 바인딩

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
       return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name", name)   //파라미터 바인딩
                .getResultList();
    }


}
