package com.sku.fitizen.domain.challenge;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProofComment {

    private int proofCommentId;
    private int proofNo;
    private String proofComment;
    private Date commentDate;
    private String author;
    private int  challId;

}
