package com.pavbatol.tmi.post.repository;

import com.pavbatol.tmi.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Integer> {
}