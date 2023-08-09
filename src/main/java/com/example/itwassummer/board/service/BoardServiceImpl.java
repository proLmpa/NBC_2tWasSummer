package com.example.itwassummer.board.service;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.board.dto.BoardResponseDto;
import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    @Transactional(readOnly = true)
    public BoardResponseDto showBoards(Long id) {
        Board board = findBoard(id);
        return new BoardResponseDto(board);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> showAllBoards() {
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream().map(BoardResponseDto::new).toList();
    }

    @Override
    @Transactional
    public BoardResponseDto createBoards(BoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto, user);
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto requestDto, User user) {
        Board board = findBoard(id);
        confirmUser(board, user);

        board.updateBoard(requestDto.getName(), requestDto.getDescription(), requestDto.getColor());

        return new BoardResponseDto(board);
    }

    @Override
    @Transactional
    public void delete(Long id, User user) {
        Board board = findBoard(id);
        confirmUser(board, user);

        boardRepository.deleteById(id);
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND, null));
    }

    private void confirmUser(Board board, User user) {
        if(!board.getUser().getId().equals(user.getId())
                && !user.getRole().equals(UserRoleEnum.ADMIN))
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
    }
}
