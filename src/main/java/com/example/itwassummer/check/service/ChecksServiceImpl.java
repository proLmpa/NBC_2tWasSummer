package com.example.itwassummer.check.service;

import com.example.itwassummer.check.dto.ChecksRequestDto;
import com.example.itwassummer.check.dto.ChecksResponseDto;
import com.example.itwassummer.check.entity.Checks;
import com.example.itwassummer.check.repository.ChecksRepository;
import com.example.itwassummer.checklist.entity.CheckList;
import com.example.itwassummer.checklist.repository.CheckListRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChecksServiceImpl implements ChecksService {

  private final ChecksRepository checkRepository;
  private final CheckListRepository checkListRepository;

  @Override
  @Transactional
  public ChecksResponseDto save(ChecksRequestDto requestDto) {
    // 체크리스트 데이터 조회
    CheckList checkList = findCheckList(requestDto.getListId());
    Checks check = Checks.builder()
        .requestDto(requestDto)
        .build();
    check.addCheckList(checkList);

    Checks returnCheck = checkRepository.save(check);
    ChecksResponseDto responseDto = ChecksResponseDto
        .builder()
        .name(returnCheck.getName())
        .checked(returnCheck.isChecked())
        .createdAt(String.valueOf(returnCheck.getCreatedAt()))
        .modifiedAt(String.valueOf(returnCheck.getModifiedAt()))
        .build();
    return responseDto;
  }

  @Override
  @Transactional
  public ChecksResponseDto updateName(Long checkId, String name) {
    // 체크 데이터 조회
    Checks check = findCheck(checkId);

    // check 내용 수정
    check.updateName(name);
    ChecksResponseDto responseDto = ChecksResponseDto
        .builder()
        .name(check.getName())
        .checked(check.isChecked())
        .createdAt(String.valueOf(check.getCreatedAt()))
        .modifiedAt(String.valueOf(check.getModifiedAt()))
        .createdAt(String.valueOf(check.getCreatedAt()))
        .modifiedAt(String.valueOf(check.getModifiedAt()))
        .build();

    return responseDto;
  }

  @Override
  @Transactional
  public ChecksResponseDto updateCheck(Long checkId, boolean checked) {
    // 체크 데이터 조회
    Checks check = findCheck(checkId);

    // check 내용 수정
    check.updateCheck(checked);
    ChecksResponseDto responseDto = ChecksResponseDto
        .builder()
        .name(check.getName())
        .checked(check.isChecked())
        .createdAt(String.valueOf(check.getCreatedAt()))
        .modifiedAt(String.valueOf(check.getModifiedAt()))
        .build();

    return responseDto;
  }

  @Override
  @Transactional
  public void delete(Long checkId) {
    Checks check = findCheck(checkId);

    checkRepository.deleteById(check.getId());
  }

  // 체크 데이터 확인
  public Checks findCheck(Long checkId) {
    return checkRepository.findById(checkId).orElseThrow(() ->
        new CustomException(CustomErrorCode.CHECK_NOT_FOUND, null));
  }

  // 체크리스트 조회
  private CheckList findCheckList(Long listId) {
    return checkListRepository.findById(listId).orElseThrow(()
        -> new CustomException(CustomErrorCode.CHECK_LIST_NOT_FOUND, null));
  }

}
