package com.hello.jpa.jpashop.domain;

import com.hello.jpa.team.BaseEntity;

import javax.persistence.*;

//@Entity
public class Delivery extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zipcode;
    private DeliveryStatus status;

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;
}
