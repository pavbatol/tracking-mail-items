package com.pavbatol.tmi.item.model;

import com.pavbatol.tmi.item.model.enums.ItemType;
import com.pavbatol.tmi.post.model.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    ItemType type;

    @ManyToOne
    @JoinColumn(name = "post_code", nullable = false)
    Post post;

    @Column(name = "receiver_address", nullable = false)
    String receiverAddress;

    @Column(name = "receiver_name", nullable = false)
    String receiverName;
}
