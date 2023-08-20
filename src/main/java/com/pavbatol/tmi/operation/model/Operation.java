package com.pavbatol.tmi.operation.model;

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
@Table(name = "operations", uniqueConstraints = {
        @UniqueConstraint(name = "uc_operations_item_id_post_code", columnNames = {"item_id", "post_code"})
})
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @ManyToOne
    @JoinColumn(name = "post_code", nullable = false)
    Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    OperationType type;

    @Column(name = "operation_time", nullable = false)
    LockModeType timestamp;
}
