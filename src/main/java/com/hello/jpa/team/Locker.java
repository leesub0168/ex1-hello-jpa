package com.hello.jpa.team;

import javax.persistence.*;

//@Entity
public class Locker {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToOne(mappedBy = "locker", fetch = FetchType.LAZY)
    private TeamMember teamMember;

}
