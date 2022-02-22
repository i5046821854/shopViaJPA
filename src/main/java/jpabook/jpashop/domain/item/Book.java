package jpabook.jpashop.domain.item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue("B")  //구분자 정보
public class Book extends Item{

    private String author;
    private String isbn;
}
