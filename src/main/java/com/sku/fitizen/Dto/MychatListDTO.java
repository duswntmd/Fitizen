package com.sku.fitizen.Dto;

import com.sku.fitizen.domain.Trainer.Trainer;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.challenge.Challenge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MychatListDTO {

    private String userId;
    private List<Challenge> challengeList;
    private List<String> users;
    private String creatorId;



    public MychatListDTO(String userId, List<Challenge> challengeList) {
        this.userId = userId;
        this.challengeList = challengeList;
    }

    public MychatListDTO(List<String> users ,String creatorId) {
        this.users = users;
        this.creatorId = creatorId;
    }
}
