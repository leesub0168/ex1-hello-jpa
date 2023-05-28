package com.hello.jpa.team;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TeamMember extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "USER_NAME")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    // FetchType.LAZY - 지연 로딩
    // FetchType.EAGER - 즉시 로딩
    // 실무에서는 가급적 지연 로딩만 사용하는게 좋음. 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생

    // 즉시로딩은 JPQL에서 N+1 문제를 일으킨다. JPQL은 작성된 쿼리를 우선적으로 SQL로 바꿔서 실행함.
    // 그래서 쿼리가 나간 후에, 컬럼중 즉시로딩인게 있다면 쿼리가 또 나가게됨.
    // JPQL로 member를 10개 받아왔는데 즉시로딩이 있다면 쿼리 10건이 또 나가게 되는 것

    // N+1 문제 해결법
    // 1. 동적으로 원하는 값만 선택해서 fetch join으로 가져오는 방법
    // 2. 엔티티 그래프 - 어노테이션 사용
    // 3. 배치 사이즈 (N+1 -> 1+1로 해결가능)


    // @ManyToOne, @OneToOne은 기본이 즉시 로딩으로 되어있어서 LAZY로 변경하는게 좋음
    // @OneToMany, @ManyToMany는 기본이 지연로딩으로 되어있어서 상관없음.
    @JoinColumn(name = "team_id")
    private Team team;

//    @OneToOne
//    @JoinColumn(name = "LOCKER_ID")
//    private Locker locker;

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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
