package com.example.itwassummer.cardnotifications.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 카드별 알림
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardNotificationsResponseDto {
    boolean isRead;

    boolean isWatched;
}
