package com.example.itwassummer.common.file;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 파일정보를 표시하기 위한 객체
@Getter
@Setter
@ToString
@Builder
public class S3FileDto {

  private String originalFileName;
  private String uploadFileName;
  private String uploadFilePath;
  private String uploadFileUrl;

}