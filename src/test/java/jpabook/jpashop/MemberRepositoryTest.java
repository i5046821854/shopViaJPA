package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {
/*
    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional  //entitymanager가 디비의 값을 변경할 수 있도록, 테스트 끝나고 롤백함
    @Rollback(false) //테스트 끝나고 롤백안하고 커밋함
    public void testMember() throws Exception{

        Member member = new Member();
        member.setUsername("memberA");

        Long saveId = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveId);

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);  //같은 트랜잭션 안에서 영속성 컨텍스트에서 같은 id면 같은 엔티티임

    }
*/
}