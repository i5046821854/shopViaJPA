package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore //json으로 리턴될때 이 컬럼을 무시함 / 무시 안하면 양방향으로 계속 조회하는 무한루프에 빠질수도 있음
    @OneToMany(mappedBy = "member")  //읽기 전용
    private List<Order> orders = new ArrayList<>();
}
