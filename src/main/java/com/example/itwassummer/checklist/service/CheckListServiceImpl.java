package com.example.itwassummer.checklist.service;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.check.repository.ChecksRepository;
import com.example.itwassummer.checklist.dto.CheckListRequestDto;
import com.example.itwassummer.checklist.dto.CheckListResponseDto;
import com.example.itwassummer.checklist.entity.CheckList;
import com.example.itwassummer.checklist.repository.CheckListRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckListServiceImpl implements CheckListService {

  private final CheckListRepository checkListRepository;

  private final ChecksRepository checkRepository;

  private final CardRepository cardRepository;

  @Override
  public CheckListResponseDto getCheckList(Long listId) {
    CheckList checkList = this.findCheckList(listId);

    return new CheckListResponseDto(checkList);
  }

  @Override
  @Transactional
  public CheckListResponseDto save(CheckListRequestDto requestDto) {
    CheckList checkList = CheckList.builder()
        .requestDto(requestDto)
        .build();
    Card card = findCard(requestDto.getCardId());
    checkList.addCard(card);
    CheckList returnCheckList = checkListRepository.save(checkList);

    return new CheckListResponseDto(returnCheckList);
  }

  @Override
  @Transactional
  public CheckListResponseDto update(Long listId, CheckListRequestDto requestDto) {
    CheckList checkList = findCheckList(listId);

    // checkList 내용 수정
    checkList.update(requestDto);

    return new CheckListResponseDto(checkList);
  }

  @Override
  @Transactional
  public void delete(Long listId) {
    CheckList checkList = findCheckList(listId);
    for (int i = 0; i < checkList.getChecks().size(); i++) {
      checkRepository.delete(checkList.getChecks().get(i));
    }
    checkListRepository.deleteById(checkList.getId());
  }

  // 체크리스트 데이터 조회
  public CheckList findCheckList(Long listId) {
    return checkListRepository.findById(listId).orElseThrow(() ->
        new CustomException(CustomErrorCode.CHECK_LIST_NOT_FOUND, null));
  }

  // 카드 조회
  private Card findCard(Long cardId) {
    return cardRepository.findById(cardId).orElseThrow(()
        -> new CustomException(CustomErrorCode.CARD_NOT_FOUND, null));
  }
}
