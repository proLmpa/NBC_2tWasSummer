package com.example.itwassummer.card.service;

import com.example.itwassummer.card.dto.CardListResponseDto;
import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.dto.CardSearchResponseDto;
import com.example.itwassummer.card.dto.CardViewResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.cardmember.dto.CardMemberResponseDto;
import com.example.itwassummer.comment.dto.CommentResponseDto;
import com.example.itwassummer.common.security.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


public interface CardService {

  /**
   * 카드 전체 조회
   *
   * @param boardId 보드 id
   * @return CardViewResponseDto 카드 상세 정보
   */
  List<CardListResponseDto> getCardList(Long boardId, int page, int size, String sortBy, boolean isAsc);

  /**
   * 카드 상세 조회
   *
   * @param cardId 카드 id
   * @return CardViewResponseDto 카드 상세 정보
   */
  CardViewResponseDto getCard(Long cardId);

  /**
   * 카드 등록
   *
   * @param requestDto 카드 등록 요청 정보
   * @param files      첨부파일 정보
   * @return CardResponseDto
   */
  CardResponseDto save(CardRequestDto requestDto, List<MultipartFile> files) throws IOException;


  /**
   * 카드 수정
   *
   * @param requestDto 카드 등록 요청 정보3
   * @param files      첨부파일 정보
   * @return CardResponseDto
   */
  CardResponseDto update(Long cardId, CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException;

  /**
   * 카드 삭제
   *
   * @param cardId 카드 등록 요청 정보
   * @return
   */
  void delete(Long cardId);


  /**
   * 카드 조회
   *
   * @param cardId 카드 데이터가 있는지 조회
   * @return Card
   */
  Card findCard(Long cardId);

  /**
   * 카드별 사용자 수정
   *
   * @param cardId    카드 데이터가 있는지 조회
   * @param emailList 이메일 목록
   * @return List<CardMemberResponseDto>
   */
  List<CardMemberResponseDto> changeCardMembers(Long cardId, String emailList);

  /**
   * 카드 마감일 수정
   *
   * @param cardId  카드 데이터가 있는지 조회
   * @param dueDate 마감일
   * @return CardResponseDto
   */
  CardResponseDto changeDueDate(Long cardId, String dueDate);

  /**
   * 카드 이동
   *
   * @param cardId 카드 데이터가 있는지 조회
   * @param order  정렬순서
   * @return CardResponseDto
   */
  CardResponseDto moveCard(Long cardId, Long order);

  /**
   * 라벨 검색 조회
   *
   * @param labelId 라벨검색
   * @param page   페이지
   * @param size   페이지별 사이즈
   * @param sortBy 정렬순서
   * @param isAsc  정렬기준 (오름차순, 내림차순)
   * @return CardResponseDto
   */
  List<CardSearchResponseDto> searchLabelList(Long labelId, int page, int size, String sortBy,
      boolean isAsc);

  /**
   * 카드를 다른 덱으로 이동
   *
   * @param deckId 카드 id
   * @param cardId 덱 id
   * @param order  정렬순서
   * @return CardResponseDto
   */
  CardResponseDto moveCardToOtherDeck(Long deckId, Long cardId, Long order);

}
