package com.hello.jpa.extendsMapping;

import javax.persistence.Entity;

@Entity
public class Album extends ItemModel {
    private String artist;
}
