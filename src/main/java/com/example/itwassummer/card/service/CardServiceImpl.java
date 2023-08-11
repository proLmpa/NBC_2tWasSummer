package com.example.itwassummer.card.service;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.card.dto.CardListResponseDto;
import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.dto.CardViewResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.cardmember.dto.CardMemberResponseDto;
import com.example.itwassummer.cardmember.entity.CardMember;
import com.example.itwassummer.cardmember.repository.CardMemberRepository;
import com.example.itwassummer.checklist.service.CheckListService;
import com.example.itwassummer.comment.dto.CommentResponseDto;
import com.example.itwassummer.comment.repository.CommentRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.common.file.FileUploader;
import com.example.itwassummer.common.file.S3FileDto;
import com.example.itwassummer.deck.entity.Deck;
import com.example.itwassummer.deck.repository.DeckRepository;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;

  private final FileUploader fileUploader;

  private final UserRepository userRepository;

  private final CardMemberRepository cardMemberRepository;

  private final CheckListService checkListService;

  private final CommentRepository commentRepository;

  private final DeckRepository deckRepository;

  private final BoardRepository boardRepository;

  @Override
  public List<CardListResponseDto> getCardList(Long boardId, int page, int size, String sortBy,
      boolean isAsc) {
    Board board = findBoard(boardId);

    Direction direction = isAsc ? Direction.ASC : Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);
    List<CardListResponseDto> lists = cardRepository.findAllByBoardId(boardId, pageable);
    return lists;
  }

  @Override
  @Transactional(readOnly = true)
  public CardViewResponseDto getCard(Long cardId) {
    Card card = this.findCard(cardId);
    CardViewResponseDto responseDto = new CardViewResponseDto(card);
    return responseDto;
  }

  @Override
  @Transactional
  public CardResponseDto save(CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException {

    Deck deck = findDeck(requestDto.getDeckId());

    // 파일 등록
    List<S3FileDto> fileDtoList = null;
    if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
      fileDtoList = fileUploader.uploadFiles(files, "files");
      requestDto.setAttachment(fileDtoList);
    }

    Card card = Card.builder()
        .requestDto(requestDto)
        .deck(deck)
        .build();
    Card returnCard = cardRepository.save(card);
    CardResponseDto responseDto = new CardResponseDto(returnCard);
    return responseDto;
  }

  @Override
  @Transactional
  public CardResponseDto update(Long cardId, CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException {
    // 파일첨부
    List<S3FileDto> fileDtoList = null;
    Card card = findCard(cardId);

    // 파일정보 불러오기
    List<S3FileDto> attachment = card.getAttachment();

    // 기존 파일 삭제
    if (attachment != null && attachment.size() > 0) {
      for (int i = 0; i < attachment.size(); i++) {
        fileUploader.deleteFile(attachment.get(i).getUploadFilePath(),
            attachment.get(i).getUploadFileName());
      }
    }

    // 파일 등록 
    if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
      fileDtoList = fileUploader.uploadFiles(files, "files");
      requestDto.setAttachment(fileDtoList);
    }

    // card 내용 수정
    card.update(requestDto);
    CardResponseDto responseDto = new CardResponseDto(card);

    return responseDto;
  }

  @Override
  @Transactional
  public void delete(Long cardId) {
    Card card = findCard(cardId);

    // 파일정보 불러오기
    List<S3FileDto> attachment = card.getAttachment();

    // 기존 파일 삭제
    if (attachment != null && attachment.size() > 0) {
      for (int i = 0; i < attachment.size(); i++) {
        fileUploader.deleteFile(attachment.get(i).getUploadFilePath(),
            attachment.get(i).getUploadFileName());
      }
    }
    //기존 체크리스트 삭제
    for (int i = 0; i < card.getCheckLists().size(); i++) {
      checkListService.delete(card.getCheckLists().get(i).getId());
    }

    cardRepository.deleteById(card.getId());
  }

  public Card findCard(Long cardId) {
    return cardRepository.findById(cardId).orElseThrow(() ->
        new CustomException(CustomErrorCode.CARD_NOT_FOUND, null));
  }

  @Override
  @Transactional
  public List<CardMemberResponseDto> changeCardMembers(Long cardId, String emailList) {
    List<CardMemberResponseDto> result = new ArrayList<>();
    // 전체삭제
    // cardMemberRepository.deleteByCardId(cardId);
    Card nowCard = findCard(cardId);
    nowCard.getCardMembers().clear();
    if (!emailList.isEmpty() && emailList != null) {
      List<String> emailArrays = Arrays.stream(emailList.split(",")).toList();

      for (int i = 0; i < emailArrays.size(); i++) {
        Optional<User> optionalUser = userRepository.findByEmail(emailArrays.get(i));
        if (optionalUser.isPresent()) {
          CardMember cardMember = new CardMember(nowCard, optionalUser.get());
          CardMember returnCardMember = cardMemberRepository.save(cardMember);
          result.add(new CardMemberResponseDto(returnCardMember));
        }
      }
    }
    return result;
  }

  @Override
  @Transactional
  public CardResponseDto changeDueDate(Long cardId, String dueDate) {
    Card card = findCard(cardId);
    LocalDateTime parseDueDate = LocalDateTime.parse(dueDate);
    card.updateDueDate(parseDueDate);
    CardResponseDto responseDto = new CardResponseDto(card);
    return responseDto;
  }

  @Override
  @Transactional
  public CardResponseDto moveCard(Long cardId, Long order) {
    Card card = findCard(cardId);
    cardRepository.changeOrder(card, order);
    card = findCard(cardId);
    card.updateParentId(order);
    CardResponseDto responseDto = new CardResponseDto(card);
    return responseDto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentResponseDto> getCommentList(Long cardId, int page, int size, String sortBy,
      boolean isAsc) {
    Card card = findCard(cardId);
    Direction direction = isAsc ? Direction.ASC : Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);
    List<CommentResponseDto> commentList = commentRepository.findAllByCard(card, pageable).stream()
        .map(CommentResponseDto::new).toList();
    return commentList;
  }

  // 보드가 있는지 확인
  private Board findBoard(Long boardId) {
    return boardRepository.findById(boardId).orElseThrow(()
        -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND, null));
  }

  // 덱이 있는지 확인
  private Deck findDeck(Long id) {
    return deckRepository.findById(id).orElseThrow(() ->
        new CustomException(CustomErrorCode.DECK_NOT_FOUND, null)
    );
  }
}
