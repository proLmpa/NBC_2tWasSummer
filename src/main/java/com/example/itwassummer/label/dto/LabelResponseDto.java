package com.example.itwassummer.label.dto;

import com.example.itwassummer.label.entity.Label;
import lombok.Getter;

@Getter
public class LabelResponseDto {
    private Long id;
    private Long boardId;
    private String title;
    private String color;

    public LabelResponseDto(Label label) {
        this.id = label.getId();
        this.boardId = label.getBoard().getId();
        this.title = label.getTitle();
        this.color = label.getColor();
    }
}
