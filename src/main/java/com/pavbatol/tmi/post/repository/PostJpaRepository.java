package com.pavbatol.tmi.post.repository;

import com.pavbatol.tmi.post.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Integer> {

    @Query("select p from Post p " +
            "where (UPPER(:direction) = 'ASC' and p.postCode > :lastId) " +
            "   or (UPPER(:direction) = 'DESC' and p.postCode < :lastId)")
    List<Post> findAllByPagination(Integer lastId, String direction, Pageable pageable);
}