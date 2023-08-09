package com.example.itwassummer.card.dto;

import com.example.itwassummer.common.file.S3FileDto;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {

  // 카드 이름
  @NotBlank
  private String name;

  // 만기일
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime dueDate;

  // 설명
  private String description;

  // 정렬순서
  private Long parentId;

  // 첨부파일
  @Setter
  private List<S3FileDto> attachment;

}
