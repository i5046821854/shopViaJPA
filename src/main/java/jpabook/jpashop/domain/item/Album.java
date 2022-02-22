package jpabook.jpashop.domain.item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue("A")  //구분자 정보
public class Album extends Item {

    private String artist;
    private String etc;
}
