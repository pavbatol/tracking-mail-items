CREATE TABLE IF NOT EXISTS posts (
    post_code INT NOT NULL,
    post_name VARCHAR(255) NOT NULL,
    post_address VARCHAR(255) NOT NULL,
    CONSTRAINT pk_posts PRIMARY KEY (post_code),
    CONSTRAINT uk_posts_post_code UNIQUE (post_code)
);

CREATE TABLE IF NOT EXISTS items (
    item_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    post_code INT NOT NULL,
    receiver_address VARCHAR(255) NOT NULL,
    receiver_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_items PRIMARY KEY (item_id),
    CONSTRAINT fk_items_post_code_to_posts FOREIGN KEY (post_code) REFERENCES posts(post_code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS movements (
    movement_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item_id BIGINT NOT NULL,
    post_code INT NOT NULL,
    move_type VARCHAR(50) NOT NULL,
    move_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT uc_movement_item_id_post_code UNIQUE (item_id, post_code),
    CONSTRAINT fk_movements_on_item_id FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE,
    CONSTRAINT fk_movements_on_post_code FOREIGN KEY (post_code) REFERENCES posts (post_code) ON DELETE CASCADE
)

--CREATE TABLE IF NOT EXISTS operations (
--    operation_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--    item_id BIGINT NOT NULL,
--    post_code BIGINT NOT NULL,
--    operation_type VARCHAR(50) NOT NULL,
--    operation_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--    CONSTRAINT pk_operations PRIMARY KEY (operation_id),
--    CONSTRAINT uc_operations_item_id_post_code UNIQUE (item_id, post_code),
--    CONSTRAINT fk_operations_on_item_id FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE,
--    CONSTRAINT fk_operations_on_post_code FOREIGN KEY (post_code) REFERENCES posts (post_code) ON DELETE CASCADE
--)