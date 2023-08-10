package com.example.itwassummer.label.dto;

import com.example.itwassummer.label.entity.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LabelResponseDto {
    private Long id;
    private String title;
    private String color;

    public LabelResponseDto(Label label) {
        this.id = label.getId();
        this.title = label.getTitle();
        this.color = label.getColor();
    }
}
