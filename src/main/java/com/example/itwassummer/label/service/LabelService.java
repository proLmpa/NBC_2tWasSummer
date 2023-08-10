package com.example.itwassummer.label.service;

import com.example.itwassummer.label.dto.LabelRequestDto;
import com.example.itwassummer.label.dto.LabelResponseDto;

import java.util.List;

public interface LabelService {
    /**
     * 해당 보드에 생성되어 있는 모든 라벨 조회.
     *
     * @param boardId 보드 Id
     * @return 라벨의 정보들을 담은 ResponseDto들의 List
     */
    List<LabelResponseDto> getLabels(Long boardId);

    /**
     * 해당 보드 안에서 사용될 라벨을 생성. (카드에 라벨 등록 아님)
     *
     * @param requestDto 라벨 생성에 필요한 정보. title, color코드
     * @param boardId    라벨이 생성될 보드의 Id
     */
    LabelResponseDto createLabel(LabelRequestDto requestDto, Long boardId);

    /**
     * 해당 라벨의 정보를 수정.
     *
     * @param labelId    라벨의 Id값
     * @param requestDto 라벨의 수정하고자 하는 정보.
     */
    LabelResponseDto editLabel(Long labelId, LabelRequestDto requestDto);

    /**
     * 해당 라벨을 삭제.
     *
     * @param labelId 라벨의 Id값
     */
    void deleteLabel(Long labelId);
}
