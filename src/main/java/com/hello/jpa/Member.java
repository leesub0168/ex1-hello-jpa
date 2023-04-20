package com.hello.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table // 매핑해야할 테이블의 이름과 다른경우 @Table에 name을 세팅해주면 됨.
public class Member {
    @Id
    private Long id;
    private String name;

    public Member() {
    }

    public Member(Long id, String name) { // Entity로 등록된 클래스에서 파라미터를 받는 생성자를 만들려면, 기본 생성자도 만들어 줘야함.
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
