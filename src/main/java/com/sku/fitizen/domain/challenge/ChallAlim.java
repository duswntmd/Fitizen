package com.sku.fitizen.domain.challenge;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallAlim {
    private int alimId;       // 알림 ID
    private String userId;    // 사용자 ID
    private int messageId;    // 메시지 ID
    private int seen;         // 알림 확인 여부 (0: 미확인, 1: 확인됨)
}