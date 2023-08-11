package com.example.itwassummer.card.repository;

import com.example.itwassummer.card.entity.Card;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.itwassummer.card.entity.QCard.card;

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
    return 1;
  }
}
