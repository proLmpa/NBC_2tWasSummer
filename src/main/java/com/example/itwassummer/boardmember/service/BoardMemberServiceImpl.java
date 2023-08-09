package com.example.itwassummer.boardmember.service;

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

@Service
@RequiredArgsConstructor
public class BoardMemberServiceImpl implements BoardMemberService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    @Override
    @Transactional
    public void inviteBoardMember(Long boardId, Long userId, User user) {
        Board board = findBoard(boardId);
        User invitee = findUser(userId);
        confirmUser(board, user);

        BoardMember boardMember = boardMemberRepository.findByBoardAndUser(board, invitee).orElse(null);

        if (boardMember == null) {
            boardMember = new BoardMember(invitee, board);
            boardMemberRepository.save(boardMember);
        } else {
            throw new CustomException(CustomErrorCode.BOARD_MEMBER_ALREADY_EXISTS, null);
        }
    }

    @Override
    @Transactional
    public void deleteBoardMember(Long boardId, Long userId, User user) {
        Board board = findBoard(boardId);
        User invitee = findUser(userId);

        confirmUser(board, user);

        BoardMember boardMember = boardMemberRepository.findByBoardAndUser(board, invitee).orElse(null);
        if (boardMember != null) {
            boardMemberRepository.deleteById(boardMember.getId());
        } else {
            throw new CustomException(CustomErrorCode.BOARD_MEMBER_NOT_EXISTS, null);
        }
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND, null));
    } // 해당 ErrorCode가 존재하지 않으면 CustomErrorCode에 추가하셔야 합니다.

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));
    }

    private void confirmUser(Board board, User user) {
        if(!board.getUser().getId().equals(user.getId())
            && user.getRole().equals(UserRoleEnum.ADMIN))
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
    }
}
