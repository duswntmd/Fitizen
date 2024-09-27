package com.sku.fitizen.domain.Trainer;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ConsultMessage {

    private int messageId;
    private int consultId;
    private String senderId;
    private LocalDateTime sentAt;
    private String message;
    private String img;
    private String uImg;

}
