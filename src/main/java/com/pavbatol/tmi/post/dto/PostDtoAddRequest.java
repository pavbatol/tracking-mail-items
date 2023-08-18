package com.pavbatol.tmi.post.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Value
public class PostDtoAddRequest {
    @NotNull
    @PositiveOrZero
    Integer postCode;

    @NotBlank
    String postName;

    @NotBlank
    String postAddress;
}
