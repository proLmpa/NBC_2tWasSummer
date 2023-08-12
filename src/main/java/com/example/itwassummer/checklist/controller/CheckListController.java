package com.example.itwassummer.checklist.controller;

import com.example.itwassummer.checklist.dto.CheckListRequestDto;
import com.example.itwassummer.checklist.dto.CheckListResponseDto;
import com.example.itwassummer.checklist.service.CheckListService;
import com.example.itwassummer.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "체크리스트 API", description = "체크리스트 기능과 관련된 API 정보를 담고 있습니다.")
public class CheckListController {

  public CheckListController(CheckListService checkListService) {
    this.checkListService = checkListService;
  }

  private final CheckListService checkListService;

  @Operation(summary = "체크리스트 상세조회", description = "체크리스트 id를 넘겨 받아 체크리스트의 상세 정보를 표시")
  @GetMapping(value = "/checklists/{listId}")
  @ResponseBody
  public ResponseEntity view(@PathVariable("listId") Long listId) {
    CheckListResponseDto responseDto = checkListService.getCheckList(listId);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @Operation(summary = "체크리스트 등록", description = "CheckListRequestDto를 통해 체크리스트정보를 전달 받은 후 DB에 저장하고 성공 메시지를 반환합니다.")
  @PostMapping(value = "/checklists")
  public ResponseEntity addCheckList(@Valid @RequestBody CheckListRequestDto requestDto
  ) {
    CheckListResponseDto returnDto = checkListService.save(requestDto);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

  @Operation(summary = "체크리스트 수정", description = "CheckListRequestDto를 통해 체크리스트정보를 전달 받은 후 DB에 저장하고 성공 메시지를 반환합니다.")
  @PutMapping("/checklists/{listId}")
  public ResponseEntity updateCheckList(@PathVariable("listId") Long listId,
      @Valid @RequestBody CheckListRequestDto requestDto
  ) {
    CheckListResponseDto returnDto = checkListService.update(listId, requestDto);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

  @Operation(summary = "체크리스트 삭제", description = "id 값을 통해 삭제")
  @DeleteMapping("/checklists/{listId}")
  public ResponseEntity<ApiResponseDto> deleteCheckList(@PathVariable("listId") Long listId) {
    checkListService.delete(listId);
    return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "삭제 성공"));
  }
}
