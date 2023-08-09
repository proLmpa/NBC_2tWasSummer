package com.example.itwassummer.card.service;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.cardmember.dto.CardMemberResponseDto;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CardService {

  /**
   * 카드 등록
   * @param requestDto 카드 등록 요청 정보
   * @param files 첨부파일 정보
   * @return
   */
  CardResponseDto save(CardRequestDto requestDto, List<MultipartFile> files) throws IOException;


  /**
   * 카드 수정
   * @param requestDto 카드 등록 요청 정보3
   * @param files 첨부파일 정보
   * @return
   */
  CardResponseDto update(Long cardId, CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException;

  /**
   * 카드 삭제
   * @param cardId 카드 등록 요청 정보
   * @return
   */
  void delete(Long cardId);


  /**
   * 카드 조회
   * @param cardId 카드 데이터가 있는지 조회
   * @return
   */
  Card findCard(Long cardId);

  /**
   * 카드별 사용자 수정
   * @param cardId 카드 데이터가 있는지 조회
   * @param emailList 이메일 목록
   * @return
   */
  List<CardMemberResponseDto> changeCardMembers(Long cardId, String emailList);

  /**
   * 카드 마감일 수정
   * @param cardId 카드 데이터가 있는지 조회
   * @param dueDate 마감일 
   * @return
   */
  CardResponseDto changeDueDate(Long cardId, String dueDate);

  /**
   * 카드 마감일 수정
   * @param cardId 카드 데이터가 있는지 조회
   * @param order 정렬순서
   * @return
   */
  CardResponseDto moveCard(Long cardId, Long order);
}
