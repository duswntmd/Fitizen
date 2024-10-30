package com.sku.fitizen.domain.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultAlim {
    private String userId;    // 사용자 ID
    private int messageId;    // 메시지 ID
    private int seen;         // 알림 확인 여부 (0: 미확인, 1: 확인됨)
}
