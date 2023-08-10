package com.example.itwassummer.boardmember.service;

import com.example.itwassummer.user.entity.User;

public interface BoardMemberService {
    /*
     * 보드에 사용자 초대
     * @param boardId 초대할 보드의 ID
     * @param userId 초대할 사용자의 ID
     * @param user 초대 주체인 사용자의 user 정보
     */
    void inviteBoardMember(Long boardId, Long userId, User user);

    /*
     * 보드에서 사용자 제거
     * @param boardId 보드의 ID
     * @param userId 보드에서 제거할 사용자의 ID
     * @param user 제거 주체인 사용자의 user 정보
     */
    void deleteBoardMember(Long boardId, Long userId, User user);
}
