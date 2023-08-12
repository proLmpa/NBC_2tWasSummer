package com.example.itwassummer.check.service;

import com.example.itwassummer.check.dto.ChecksRequestDto;
import com.example.itwassummer.check.dto.ChecksResponseDto;


public interface ChecksService {

  /**
   * 체크 등록
   *
   * @param requestDto 카드일정체크 등록 요청 정보
   * @return CardResponseDto
   */
  ChecksResponseDto save(ChecksRequestDto requestDto);


  /**
   * 체크 수정
   *
   * @param name 체크박스 이름
   * @return CardResponseDto
   */
  ChecksResponseDto updateName(Long checkId, String name);


  /**
   * 체크여부 수정
   *
   * @param checked 체크여부
   * @return CardResponseDto
   */
  ChecksResponseDto updateCheck(Long checkId, boolean checked);

  /**
   * 체크 삭제
   *
   * @param checkId 체크 id
   * @return
   */
  void delete(Long checkId);

}
