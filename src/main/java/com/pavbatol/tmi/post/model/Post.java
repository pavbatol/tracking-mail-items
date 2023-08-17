package com.pavbatol.tmi.post.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @Column(name = "post_code", unique = true, nullable = false)
    Integer postCode;

    @Column(name = "post_name", nullable = false)
    String postName;

    @Column(name = "post_address", nullable = false)
    String postAddress;
}
