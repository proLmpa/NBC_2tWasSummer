package com.example.itwassummer.common.file;


import com.example.itwassummer.common.util.JsonConverter;
import lombok.*;

// 파일정보를 표시하기 위한 객체
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3FileDto {

  private String originalFileName;
  private String uploadFileName;
  private String uploadFilePath;
  private String uploadFileUrl;

  public static class S3FileDtoConverter extends JsonConverter<S3FileDto> {}
}