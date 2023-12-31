package com.example.itwassummer.deck.repository;

import com.example.itwassummer.deck.entity.Deck;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.itwassummer.deck.entity.QDeck.deck;

@Repository
@RequiredArgsConstructor
public class DeckRepositoryImpl implements DeckCustomRepository{
	private final JPAQueryFactory queryFactory;

	@Override
	public List<Deck> findAllDecksByBoardId(Long boardId) {
		return  queryFactory.selectFrom(deck)
				.leftJoin(deck.parent)
				.fetchJoin()
				.where(deck.board.id.eq(boardId))
				.where(deck.isDeleted.isFalse())
				.orderBy(deck.parent.id.asc().nullsFirst())
				.fetch();
	}
}
