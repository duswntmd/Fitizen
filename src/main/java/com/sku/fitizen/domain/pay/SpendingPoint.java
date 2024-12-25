package com.sku.fitizen.domain.pay;


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
   private int status;
   private String detail;

   public SpendingPoint(String userId, int spendingPoint, int status, String detail) {
      this.userId = userId;
      this.spendingPoint = spendingPoint;
      this.status = status;
      this.detail = detail;
   }

}

