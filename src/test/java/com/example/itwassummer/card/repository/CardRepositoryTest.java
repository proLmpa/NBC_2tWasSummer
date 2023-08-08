package com.example.itwassummer.card.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.common.config.QueryDslConfig;
import com.example.itwassummer.user.entity.UserRoleEnum;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-ds.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
@Rollback(false)
public class CardRepositoryTest {
  @Autowired
  CardRepository cardRepository;


  @Test
  void insertTest() {
    String name = "예시카드2";
    Long parentId = Long.valueOf(1);
    String description = "예시카드입니다.";
    LocalDateTime now = LocalDateTime.now();
    // given
    var cards = new Card(name, now, description, parentId);
    // when
    Card newCard = cardRepository.save(cards);
    // then
    assertThat(name).isEqualTo(newCard.getName());
  }

}
