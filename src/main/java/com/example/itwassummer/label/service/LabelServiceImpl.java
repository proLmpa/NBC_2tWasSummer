package com.example.itwassummer.label.service;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.label.dto.LabelRequestDto;
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
    private final BoardRepository boardRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LabelResponseDto> getLabels(Long boardId) {
        findBoard(boardId);

        List<Label> labels = labelRepository.findAllByBoard_Id(boardId);
        return labels.stream().map(LabelResponseDto::new).toList();
    }

    @Override
    @Transactional
    public LabelResponseDto createLabel(LabelRequestDto requestDto, Long boardId) {
        Board board = findBoard(boardId);
        findDuplicateLabel(requestDto.getTitle());

        Label label = new Label(requestDto);
        label.setBoard(board);
        labelRepository.save(label);

        return new LabelResponseDto(label);
    }

    @Override
    @Transactional
    public LabelResponseDto editLabel(Long labelId, LabelRequestDto requestDto) {
        findDuplicateLabel(requestDto.getTitle());

        Label label = findLabel(labelId);
        label.editLabel(requestDto);

        return new LabelResponseDto(label);
    }

    @Override
    @Transactional
    public void deleteLabel(Long labelId) {
        Label label = findLabel(labelId);
        labelRepository.delete(label);
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException(CustomErrorCode.BOARD_NOT_FOUND, null));
    }

    private void findDuplicateLabel(String title) {
        if(labelRepository.existsByTitle(title))
                throw new CustomException(CustomErrorCode.LABEL_ALREADY_EXISTS, null);
    }

    private Label findLabel(Long labelId) {
        return labelRepository.findById(labelId).orElseThrow(() ->
                new CustomException(CustomErrorCode.LABEL_NOT_FOUND, null));
    }
}
