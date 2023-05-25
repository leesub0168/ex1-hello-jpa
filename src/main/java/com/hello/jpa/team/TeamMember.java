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

    @ManyToOne
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
