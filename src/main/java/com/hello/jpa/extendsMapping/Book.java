package com.hello.jpa.extendsMapping;

import javax.persistence.Entity;

@Entity
public class Book extends ItemModel{
    private String author;
    private String isbn;
}
