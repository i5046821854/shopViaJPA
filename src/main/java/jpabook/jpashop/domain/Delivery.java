package jpabook.jpashop.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Data
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)  //enum 타입을 스트링으로? 아님 숫자형으로 ? => 되도록이면 무조건 스트링으로!
    private DeliveryStatus status;


}
