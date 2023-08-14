package com.example.itwassummer.cardlabel.service;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.cardlabel.entity.CardLabel;
import com.example.itwassummer.cardlabel.repository.CardLabelRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
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
                -> new CustomException(CustomErrorCode.CARD_NOT_FOUND, null));
        Label label = labelRepository.findById(labelId).orElseThrow(()
                -> new CustomException(CustomErrorCode.LABEL_NOT_FOUND, null));

        CardLabel cardLabel = cardLabelRepository.findByCard_IdAndLabel_Id(cardId, labelId).orElse(null);
        if(cardLabel != null)
            throw new CustomException(CustomErrorCode.LABEL_ALREADY_EXISTS, null);

        cardLabel = new CardLabel(card, label);
        cardLabelRepository.save(cardLabel);

        return "카드에 라벨 등록 완료";
    }

    @Override
    @Transactional
    public String deleteCardLabel(Long cardId, Long labelId) {
        CardLabel cardLabel = cardLabelRepository.findByCard_IdAndLabel_Id(cardId, labelId).orElseThrow(
                () -> new CustomException(CustomErrorCode.LABEL_NOT_FOUND, null)
        );
        cardLabelRepository.delete(cardLabel);

        return "라벨 해제 완료";
    }
}
