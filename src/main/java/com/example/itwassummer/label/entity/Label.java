package com.example.itwassummer.label.entity;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.label.dto.LabelCreateRequestDto;
import com.example.itwassummer.label.dto.LabelEditRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "label")
public class Label {

    ////컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String color;


    public Label(Long id, String title, String color) {
        this.id = id;
        this.title = title;
        this.color = color;
    }

    ////생성자 - 약속된 형태로만 생성가능하도록 합니다.
    public Label(LabelCreateRequestDto requestDto) {
        this.id = requestDto.getId();
        this.title = requestDto.getTitle();
        this.color = requestDto.getColor();
    }

    ////연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label", nullable = false)
    private Board board;


    ////연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.

    public void addBoard(Board board) {
        this.board = board;
//        board.getLabels.add(this); todo Board 작업이후 수정 예정
    }


    //// 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
    public void editLabel(LabelEditRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.color = requestDto.getColor();
    }
}
