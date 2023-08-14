package com.example.itwassummer.card.repository;

import com.example.itwassummer.card.dto.CardListResponseDto;
import com.example.itwassummer.card.dto.CardSearchResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.itwassummer.board.entity.QBoard.board;
import static com.example.itwassummer.card.entity.QCard.card;
import static com.example.itwassummer.cardlabel.entity.QCardLabel.cardLabel;
import static com.example.itwassummer.deck.entity.QDeck.deck;
import static com.example.itwassummer.label.entity.QLabel.label;

// 커스텀 레포지토리
@RequiredArgsConstructor
@Repository
public class CardRepositoryImpl implements CustomCardRepository {

    private final JPAQueryFactory queryFactory;

    // 추후 재귀 방식으로 다량의 건을 업데이트 할 경우에 성능저하를 최소화 할 수 있도록 개선 필요함
    @Override
    public void changeOrder(Card selectCard, long order) {

        // 바꾸려는 숫자보다 같거나 크면 +1
        queryFactory.update(card)
                .set(card.parentId, card.parentId.add(1))
                .where((card.parentId.gt(order)
                        .or(card.parentId.eq(order)))
                        .and(card.deck.id.eq(selectCard.getDeck().getId()))
                )
                .execute();
    }

    @Override
    public List<CardListResponseDto> findAllByBoardId(Long boardId, Pageable pageable) {

        var query = queryFactory.select(card)
                .from(board)
                .join(board.deckList, deck)
                .join(deck.cardList, card)
                .where(
                        board.id.eq(boardId)
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(cardSort(pageable));

        var lists = query.fetch();


        return lists.stream().map(CardListResponseDto::new).toList();
    }

    @Override
    public List<CardSearchResponseDto> findAllByLabelId(Long labelId, Pageable pageable) {

        var query = queryFactory.select(card)
                .from(label)
                .join(label.cardLabels, cardLabel)
                .join(cardLabel.card, card)
                .where(
                        label.id.eq(labelId)
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(cardSort(pageable));

        var lists = query.fetch();

        return lists.stream().map(CardSearchResponseDto::new).toList();
    }

    private OrderSpecifier<?> cardSort(Pageable page) {
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (!page.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                String property = order.getProperty();
                if (property.equals("name")) {
                    return new OrderSpecifier<>(direction, card.name);
                } else if (property.equals("createdAt")) {
                    return new OrderSpecifier<>(direction, card.createdAt);
                }
            }
        }
        return null;
    }


}
