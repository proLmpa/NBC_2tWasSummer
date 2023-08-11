package com.example.itwassummer.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String color; // default 지정?
}