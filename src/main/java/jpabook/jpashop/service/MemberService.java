package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)      //트랜젝션 안에서 데이터를 변경해야하므로 //조회 전용에는 이걸 넣어주면 성능 향상
@RequiredArgsConstructor  //final인 필드만 생성자 만들어줌
public class MemberService {

//    @Autowired
    private final MemberRepository memberRepository;

/*
    @Autowired  //setter로 autowire해주면 리포지토리를 유연하게 가져갈 수 있음, 그러나 런타임에 리포지토리가 바뀔수 있음
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
*/

/*
    //@Autowired //로딩시점에 바인딩이 끝나므로 안정성 좋음 / 하지만 @AllArgConstructor나 @RequiredArgsConstructor가 자동으로 만들어줌
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
*/

    @Transactional(readOnly = false)  //읽기 전용 해제
    //회원 가입
    public Long join(Member member){
        validateDuplicateMember(member);  //중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
    
    @Transactional
    public void update(Long id, String name){   //변경 감지 기법
        Member member = memberRepository.findOne(id);
        member.setName(name);

        //반환 타입은 Member로 주지 않는 것이 좋음, 그렇게되면 command와 쿼리가 분리되지 않으므로 (member를 업데이트 함과 동시에 조회를 하게 되는 상황)
        //그것보다는 컨트롤러에서 따로 findOne으로 찾아서 MEMBER를 반환받는게 나음
    }
}
