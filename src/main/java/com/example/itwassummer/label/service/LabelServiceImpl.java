package com.example.itwassummer.label.service;

import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.label.dto.LabelCreateRequestDto;
import com.example.itwassummer.label.dto.LabelEditRequestDto;
import com.example.itwassummer.label.dto.LabelResponseDto;
import com.example.itwassummer.label.entity.Label;
import com.example.itwassummer.label.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
//    private final BoardRepository boardRepository; todo Board 적용 후 수정 예정


    @Override
    @Transactional(readOnly = true)
    public List<LabelResponseDto> getLabels(Long boardId) {

        List<Label> labels = labelRepository.findAllByBoard_Id(boardId);
        List<LabelResponseDto> labelResponseDtos = labels.stream().map(LabelResponseDto::new).toList();

        return labelResponseDtos;
    }

    @Override
    @Transactional
    public String createLabel(LabelCreateRequestDto requestDto, Long boardId) {

//        Board board = boardRepository.findById(boardId); //todo Board 머지 후 수정

        Label newLabel = new Label(requestDto);
//        newLabel.addBoard(board); //todo Board 머지 후 수정
        labelRepository.save(newLabel);

        return "라벨 생성 완료";
    }

    @Override
    @Transactional
    public String editLabel(Long labelId, LabelEditRequestDto requestDto) {

        Label label = findLabel(labelId);
        label.editLabel(requestDto);

        return "라벨 수정 완료.";
    }

    @Override
    @Transactional
    public String deleteLabel(Long labelId) {

        Label label = findLabel(labelId);
        labelRepository.delete(label);

        return "라벨 삭제 완료.";
    }

    private Label findLabel(Long labelId) {
        return labelRepository.findById(labelId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.LABEL_NOT_FOUND, null));
    }
}
