package com.example.itwassummer.deck.entity;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "deck")
public class Deck extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER) // 덱은 항상 보드와 같이 조회되므로
	@JoinColumn(name = "board_id")
	private Board board;

	@OneToOne(fetch = FetchType.EAGER) // 덱들은 항상 같이 조회되므로
	@JoinColumn(name = "parent_id")
	private Deck parent;

	@OneToMany(mappedBy = "deck", orphanRemoval = true)
	private List<Card> cardList = new ArrayList<>();

	private Boolean isDeleted = false;

	public Deck(String name, Board board) {
		this.name = name;
		this.board = board;
	}

	public void updateParent(Deck parent) {
		this.parent = parent;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void deleteDeck() {
		this.isDeleted = true;
	}

	public void restoreDeck() {
		this.isDeleted = false;
	}
}
