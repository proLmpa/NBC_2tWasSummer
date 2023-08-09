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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    // 단일 보드 조회
    @Override
    public BoardResponseDto showBoards(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(()
                -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND, null));
        return new BoardResponseDto(board);
    }

    // 보드 목록 조회
    @Override
    public List<BoardResponseDto> showAllBoards() {
        List<Board> boardList = boardRepository.findAll();

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            boardResponseDtoList.add(new BoardResponseDto(board));
        }
        return null;
    }

    @Override
    public void createBoards(BoardRequestDto requestDto, User user) {
        if (requestDto.getBoard_name().isEmpty()) {
            throw new CustomException(CustomErrorCode.BOARD_NAME_ISNULL, null);
        }
        Board board = new Board(requestDto, user);
        boardRepository.save(board);
    }

    /*
    보드 수정 - 보드 이름 / 배경 색상 / 설명 변경 가능
     */
    @Override
    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto requestDto, User user) {
        Board board = confirmUser(id, user);

        if (!requestDto.getBoard_name().isEmpty()) {
            board.setBoard_name(requestDto.getBoard_name());
        }
        if (!requestDto.getDescription().isEmpty()) {
            board.setDescription(requestDto.getDescription());
        }
        if (!requestDto.getColor().isEmpty()) {
            board.setColor(requestDto.getColor());
        }

        return new BoardResponseDto(board);
    }

    @Override
    @Transactional
    public void delete(Long id, User user) {
        Board board = confirmUser(id, user);
        boardRepository.delete(board);
    }

    // 보드 수정, 삭제 시 유저 권한 확인 메서드
    private Board confirmUser(Long id, User user) {
        // 글 가져오기
        Board board = boardRepository.findById(id).orElseThrow(()
                -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND, null));

        if (user.getRole() == UserRoleEnum.USER) { // 일반 회원
            if (board.getUser().getId() == user.getId()) { // 작성자 검증
                return board;
            } else {
                throw new CustomException(CustomErrorCode.BOARD_NAME_ISNULL, null);
            }
        } else { // 관리자 회원
            return board;
        }
    }
}
