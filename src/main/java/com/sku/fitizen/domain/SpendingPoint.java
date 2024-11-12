package com.sku.fitizen.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpendingPoint {

   private int spendingId;
   private String userId;
   private int spendingPoint;
   private Date spentAt;


   public SpendingPoint(String userId, int spendingPoint) {
      this.userId = userId;
      this.spendingPoint = spendingPoint;
   }
}

