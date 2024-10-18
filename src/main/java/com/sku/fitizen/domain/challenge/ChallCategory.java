package com.sku.fitizen.domain.challenge;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallCategory {

    int categoryId ;
    String categoryName ;

    List<Challenge> challenges ;

    @Override
    public String toString() {
        return "ChallCategory [categoryId=" + categoryId + ", categoryName=" + categoryName
                + ", challenges=" + challenges + "]";
    }
}
