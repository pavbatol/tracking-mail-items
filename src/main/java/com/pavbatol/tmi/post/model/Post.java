package com.pavbatol.tmi.post.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postCode, post.postCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postCode);
    }
}
