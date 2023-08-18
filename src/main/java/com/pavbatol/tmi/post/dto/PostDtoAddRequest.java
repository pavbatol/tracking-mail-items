package com.pavbatol.tmi.post.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class PostDtoAddRequest {
    @NotNull
    Integer postCode;

    @NotBlank
    String postName;

    @NotBlank
    String postAddress;
}
