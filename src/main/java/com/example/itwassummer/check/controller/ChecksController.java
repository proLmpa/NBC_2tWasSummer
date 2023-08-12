package com.example.itwassummer.check.controller;


import com.example.itwassummer.check.dto.ChecksRequestDto;
import com.example.itwassummer.check.dto.ChecksResponseDto;
import com.example.itwassummer.check.service.ChecksService;
import com.example.itwassummer.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "체크 API", description = "체크 기능과 관련된 API 정보를 담고 있습니다.")
public class ChecksController {

  public ChecksController(ChecksService checkService) {
    this.checkService = checkService;
  }

  private final ChecksService checkService;

  @Operation(summary = "체크 등록", description = "CheckRequestDto를 통해 체크정보를 전달 받은 후 DB에 저장하고 성공 메시지를 반환합니다.")
  @PostMapping(value = "/checks")
  public ResponseEntity addCheck(@Valid @RequestBody ChecksRequestDto requestDto) {
    ChecksResponseDto returnDto = checkService.save(requestDto);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

  @Operation(summary = "체크 수정", description = "체크정보를 전달 받은 후 DB에 저장하고 성공 메시지를 반환합니다.")
  @PatchMapping(value = "/checks/{checkId}/name")
  public ResponseEntity updateName(@PathVariable("checkId") Long checkId,
      @RequestParam String name) {
    ChecksResponseDto returnDto = checkService.updateName(checkId, name);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

  @Operation(summary = "체크 수정", description = "체크여부를 전달 받은 후 DB에 저장하고 성공 메시지를 반환합니다.")
  @PatchMapping(value = "/checks/{checkId}/isChecked")
  public ResponseEntity updateCheck(@PathVariable("checkId") Long checkId,
      @RequestParam boolean checked) {
    ChecksResponseDto returnDto = checkService.updateCheck(checkId, checked);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

  @Operation(summary = "체크 삭제", description = "id 값을 통해 삭제")
  @DeleteMapping("/checks/{checkId}")
  public ResponseEntity<ApiResponseDto> deleteCheck(@PathVariable("checkId") Long checkId) {
    checkService.delete(checkId);
    return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "삭제 성공"));
  }

}
