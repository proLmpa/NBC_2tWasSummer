package com.example.itwassummer.cardlabel.service;

public interface CardLabelService {

    /**
     * 카드와 라벨 객체(외래키)갖고 있는 카드라벨 객체를 만들어서 DB에 저장
     *
     * @param cardId  카드의 Id값
     * @param labelId 라벨의 Id값
     * @return 완료 메시지
     */
    String createCardLabel(Long cardId, Long labelId);

    /**
     * 카드에 지정된 라벨을 등록 해제해준다. DB상에서는 CardLabel 데이터를 삭제한다.
     *
     * @param cardId
     * @param labelId
     * @return 완료메시지
     */
    String deleteCardLabel(Long cardId, Long labelId);
}
