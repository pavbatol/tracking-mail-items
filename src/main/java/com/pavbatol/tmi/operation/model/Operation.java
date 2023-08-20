package com.pavbatol.tmi.operation.model;

import com.pavbatol.tmi.item.model.Item;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.pavbatol.tmi.post.model.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Accessors(chain = true)
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
    @Column(name = "type", nullable = false)
    OperationType type;

    @Column(name = "operated_on", nullable = false)
    LocalDateTime operatedOn;
}
