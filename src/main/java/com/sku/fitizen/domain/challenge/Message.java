package com.sku.fitizen.domain.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private int messageId;
    private int roomId;
    private String senderId;

    private LocalDateTime sentAt;
    private String message;

    private String img;

    private String uImg;

}
