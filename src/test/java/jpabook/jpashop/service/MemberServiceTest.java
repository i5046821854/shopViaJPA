package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;
//외부 디비 말고 메모리에 테스트 데이터를 조작하고 싶다? >> test 밑에 resources 디렉토리를 만들고 그 밑에 application.yml을 만들어줌 그 뒤는 해당 파일 참조
@SpringBootTest //스프링의 기능을 이용하기 위해 test에 넣어줌  
@Transactional  //자동 롤백
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
//    @Rollback(false)  //자동 롤백 해제
    public void 회원가입(){
        Member member = new Member();
        member.setName("kim");

        Long saveId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    public void 중복회원예약( ) throws Exception{
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        memberService.join(member1);
        try {
            memberService.join(member2);
        } catch(IllegalStateException e)
        {
            return;
        }
        fail("예외 발생!");
    }

}
