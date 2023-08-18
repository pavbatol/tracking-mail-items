package com.pavbatol.tmi.post.dto;

import lombok.Value;

import javax.validation.constraints.Pattern;

@Value
public class PostDtoUpdate {
    @Pattern(regexp = ".*\\S.*")
    String postName;

    @Pattern(regexp = ".*\\S.*")
    String postAddress;
}
