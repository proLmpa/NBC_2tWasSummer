package com.example.itwassummer.board.dto;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.user.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<UserResponseDto> members;

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.name = board.getName();
        this.description = board.getDescription();
        this.color = board.getColor();
        this.boardCreator = board.getUser().getNickname();
        this.members = board.getBoardMembers().stream().map(member -> new UserResponseDto(member.getUser())).toList();
    }
}
