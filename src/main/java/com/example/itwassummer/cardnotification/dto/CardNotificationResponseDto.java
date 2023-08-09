package com.example.itwassummer.cardnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 카드별 알림
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardNotificationResponseDto {
    boolean isRead;

    boolean isWatched;
}
