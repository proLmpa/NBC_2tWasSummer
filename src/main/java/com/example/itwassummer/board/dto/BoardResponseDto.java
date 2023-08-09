package com.example.itwassummer.board.dto;

import com.example.itwassummer.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {
    private Long boardId;

    private String name;

    private String description;

    private String color;

    private String boardCreator;

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.name = board.getName();
        this.description = board.getDescription();
        this.color = board.getColor();
        this.boardCreator = board.getUser().getNickname() + " : " + board.getUser().getEmail();
    }
}
