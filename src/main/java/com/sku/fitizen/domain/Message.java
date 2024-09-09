package com.sku.fitizen.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private int messageId;
    private int roomId;
    private String senderId;

    private Date sentAt;
    private String message;


}
