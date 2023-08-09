package com.example.itwassummer.cardlabel.service;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.cardlabel.entity.CardLabel;
import com.example.itwassummer.cardlabel.repository.CardLabelRepository;
import com.example.itwassummer.label.entity.Label;
import com.example.itwassummer.label.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardLabelServiceImpl implements CardLabelService {

    private final CardLabelRepository cardLabelRepository;
    private final CardRepository cardRepository;
    private final LabelRepository labelRepository;

    @Override
    @Transactional
    public String createCardLabel(Long cardId, Long labelId) {

        Card card = cardRepository.findById(cardId).orElseThrow(()
                -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다."));
        Label label = labelRepository.findById(labelId).orElseThrow(()
                -> new IllegalArgumentException("해당 라벨을 찾을 수 없습니다."));

        CardLabel cardLabel = new CardLabel(card, label);
        cardLabelRepository.save(cardLabel);

        return "카드에 라벨 등록 완료";
    }

    @Override
    @Transactional
    public String deleteCardLabel(Long cardId, Long labelId) {

        CardLabel cardLabel = cardLabelRepository.findByCard_IdAndLabel_Id(cardId, labelId);
        cardLabelRepository.delete(cardLabel);

        return "라벨 해제 완료";
    }
}
