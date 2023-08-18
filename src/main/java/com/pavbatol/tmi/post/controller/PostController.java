package com.pavbatol.tmi.post.controller;

import com.pavbatol.tmi.post.dto.PostDtoAddRequest;
import com.pavbatol.tmi.post.dto.PostDto;
import com.pavbatol.tmi.post.dto.PostDtoUpdate;
import com.pavbatol.tmi.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Post office", description = "API for working with Posts")
@RequestMapping("/posts")
public class PostController {
    private final PostService service;

    @PostMapping
    @Operation(summary = "add", description = "adding a Post")
    ResponseEntity<PostDto> add(@Valid @RequestBody PostDtoAddRequest dto) {
        log.debug("POST (add) with dto: {}", dto);
        PostDto body = service.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "update", description = "Post update")
    ResponseEntity<PostDto> update(@PathVariable(value = "postId") Integer postId,
                                   @Valid @RequestBody PostDtoUpdate dto) {
        log.debug("PATCH (update) with postId: {}, dto: {}", postId, dto);
        PostDto body = service.update(postId, dto);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "remove", description = "deleting a Post")
    ResponseEntity<Void> remove(@PathVariable(value = "postId") Integer postId) {
        log.debug("DELETE (remove) with postId: {}", postId);
        service.remove(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}")
    @Operation(summary = "findById", description = "getting a Post by Id")
    public ResponseEntity<PostDto> findById(@PathVariable("postId") Integer postId) {
        log.debug("GET findById() with postId {}", postId);
        PostDto body = service.findById(postId);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "findAll", description = "find all Posts getting page by page")
    public ResponseEntity<List<PostDto>> findAll(
            @Min(0) @RequestParam(value = "lastPostId", defaultValue = "0") Integer lastPostId,
            @Min(1) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.debug("GET findAll() with lastPostId: {}, size: {}", lastPostId, pageSize);
        List<PostDto> body = service.findAll(lastPostId, pageSize);
        return ResponseEntity.ok(body);
    }
}
