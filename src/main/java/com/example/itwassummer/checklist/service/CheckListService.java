package com.example.itwassummer.checklist.service;

import com.example.itwassummer.checklist.dto.CheckListRequestDto;
import com.example.itwassummer.checklist.dto.CheckListResponseDto;
import java.io.IOException;

public interface CheckListService {

  /**
   * 체크리스트 상세보기
   * @param listId 체크리스트 id
   * @return CheckListViewResponseDto 체크리스트 상세 정보
   */
  CheckListResponseDto getCheckList(Long listId);

  /**
   * 체크리스트 등록
   * @param requestDto 체크리스트 등록 요청 정보
   * @return CheckListResponseDto
   */
  CheckListResponseDto save(CheckListRequestDto requestDto);


  /**
   * 체크리스트 수정
   * @param requestDto 체크리스트 등록 요청 정보3
   * @return CheckListResponseDto
   */
  CheckListResponseDto update(Long listId, CheckListRequestDto requestDto);

  /**
   * 체크리스트 삭제
   * @param listId 체크리스트 등록 요청 정보
   * @return
   */
  void delete(Long listId);

}
