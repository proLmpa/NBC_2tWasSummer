package com.example.itwassummer.common.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

// 파일첨부 기능을 수행하는 클래스
@Slf4j
@RequiredArgsConstructor
@Component
public class FileUploader {

  private final AmazonS3Client amazonS3Client;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  /**
   * S3로 파일 업로드
   */
  public List<S3FileDto> uploadFiles(List<MultipartFile> multipartFiles, String folderName) {

    List<S3FileDto> s3files = new ArrayList<>();

    String uploadFilePath = folderName + "/" + getFolderName();

    for (MultipartFile multipartFile : multipartFiles) {

      String originalFileName = multipartFile.getOriginalFilename();
      String uploadFileName = getUuidFileName(originalFileName);
      String uploadFileUrl = "";

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(multipartFile.getSize());
      objectMetadata.setContentType(multipartFile.getContentType());

      try (InputStream inputStream = multipartFile.getInputStream()) {

        String keyName = uploadFilePath + "/" + uploadFileName; // ex) 구분/년/월/일/파일.확장자

        // S3에 폴더 및 파일 업로드
        amazonS3Client.putObject(
            new PutObjectRequest(bucket, keyName, inputStream, objectMetadata));

        // TODO : 외부에 공개하는 파일인 경우 Public Read 권한을 추가, ACL 확인할 것

        // S3에 업로드한 폴더 및 파일 URL
        uploadFileUrl = amazonS3Client.getUrl(bucket, keyName).toString();

      } catch (IOException e) {
        e.printStackTrace();
        log.error("Filed upload failed", e);
      }

      s3files.add(
          S3FileDto.builder()
              .originalFileName(originalFileName)
              .uploadFileName(uploadFileName)
              .uploadFilePath(uploadFilePath)
              .uploadFileUrl(uploadFileUrl)
              .build());
    }

    return s3files;
  }

  /**
   * S3에 업로드된 파일 삭제
   */
  public String deleteFile(String uploadFilePath, String uuidFileName) {

    String result = "success";

    try {
      String keyName = uploadFilePath + "/" + uuidFileName; // ex) 구분/년/월/일/파일.확장자
      boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, keyName);
      if (isObjectExist) {
        amazonS3Client.deleteObject(bucket, keyName);
      } else {
        result = "file not found";
      }
    } catch (Exception e) {
      log.debug("Delete File failed", e);
    }

    return result;
  }

  /**
   * UUID 파일명 반환
   */
  public String getUuidFileName(String fileName) {
    String ext = fileName.substring(fileName.indexOf(".") + 1);
    return UUID.randomUUID().toString() + "." + ext;
  }

  /**
   * 년/월/일 폴더명 반환
   */
  private String getFolderName() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();
    String str = sdf.format(date);
    return str.replace("-", "/");
  }
}
