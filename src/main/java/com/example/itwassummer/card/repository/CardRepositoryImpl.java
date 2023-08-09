package com.example.itwassummer.card.repository;

import com.example.itwassummer.card.entity.Card;

import static com.example.itwassummer.card.entity.QCard.card;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// 커스텀 레포지토리
@RequiredArgsConstructor
@Repository
public class CardRepositoryImpl implements CustomCardRepository {


  private final JPAQueryFactory queryFactory;

  // 추후 재귀 방식으로 다량의 건을 업데이트 할 경우에 성능저하를 최소화 할 수 있도록 개선 필요함
  @Override
  public int changeOrder(Card selectCard, long order) {

    // 바꾸려는 숫자보다 같거나 크면 +1
    queryFactory.update(card)
        .set(card.parentId, card.parentId.add(1))
        .where(card.parentId.gt(order)
            .or(card.parentId.eq(order))
        )
        .execute();
    // 기존 id에 해당되는건은 바꾸려고하는 숫자(order)로 변경
    queryFactory.update(card)
        .set(card.parentId, order)
        .where(card.id.eq(selectCard.getId())
        )
        .execute();


    return 1;
  }
}
