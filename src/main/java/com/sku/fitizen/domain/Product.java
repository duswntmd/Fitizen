package com.sku.fitizen.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {
    int prid;
    String prname;
    int prprice;
    String prdesc;
    String primage;
    int stock;
}
