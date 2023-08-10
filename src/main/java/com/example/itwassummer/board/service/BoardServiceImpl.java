package com.example.itwassummer.board.service;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.board.dto.BoardResponseDto;
import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.boardmember.entity.BoardMember;
import com.example.itwassummer.boardmember.repository.BoardMemberRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import com.example.itwassummer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public BoardResponseDto showBoard(Long id) {
        Board board = findBoard(id);
        return new BoardResponseDto(board);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> showBoards() {
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream().map(BoardResponseDto::new).toList();
    }

    @Override
    @Transactional
    public BoardResponseDto createBoards(BoardRequestDto requestDto, User user) {
        user = findUser(user.getId());

        // 동일한 이름의 보드 존재 시 생성 불가
        if (boardRepository.findByName(requestDto.getName()).isPresent())
            throw new CustomException(CustomErrorCode.BOARD_ALREADY_EXIST, null);

        Board board = new Board(requestDto, user);
        boardRepository.save(board);

        // 보드 생성자를 보드 작업자로 등록
        BoardMember member = new BoardMember(user, board);
        boardMemberRepository.save(member);

        return new BoardResponseDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user) {
        Board board = findBoard(id);
        confirmUser(board, user);

        board.updateBoard(requestDto.getName(), requestDto.getDescription(), requestDto.getColor());

        return new BoardResponseDto(board);
    }

    @Override
    @Transactional
    public void deleteBoard(Long id, User user) {
        Board board = findBoard(id);
        confirmUser(board, user);

        boardRepository.deleteById(id);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND, null));
    }

    private void confirmUser(Board board, User user) {
        if (!board.getUser().getId().equals(user.getId())
                && !user.getRole().equals(UserRoleEnum.ADMIN))
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
    }
}
