package com.pavbatol.tmi.movement.model;

import com.pavbatol.tmi.item.model.Item;
import com.pavbatol.tmi.post.model.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "movements", uniqueConstraints = {
        @UniqueConstraint(name = "uc_movement_item_id_post_code", columnNames = {"item_id", "post_code"})
})
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @ManyToOne
    @JoinColumn(name = "post_code", nullable = false)
    Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "move_type", nullable = false)
    MoveType type;

    @Column(name = "move_time", nullable = false)
    LockModeType moveTime;
}
