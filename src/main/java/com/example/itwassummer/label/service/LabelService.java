package com.example.itwassummer.label.service;

import com.example.itwassummer.label.dto.LabelCreateRequestDto;
import com.example.itwassummer.label.dto.LabelEditRequestDto;
import com.example.itwassummer.label.dto.LabelResponseDto;

import java.util.List;

public interface LabelService {
    List<LabelResponseDto> getLabels(Long boardId);

    String createLabel(LabelCreateRequestDto requestDto, Long boardId);

    String editLabel(Long labelId, LabelEditRequestDto requestDto);

    String deleteLabel(Long labelId);
}
