package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {
    
    @NotEmpty(message = "회원 이름은 필수 입니다")  //validation 기능
    private String name;
    private String street;
    private String city;
    private String zipcode;
}
