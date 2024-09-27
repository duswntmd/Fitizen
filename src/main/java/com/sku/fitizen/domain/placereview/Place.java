package com.sku.fitizen.domain.placereview;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Place {
    private Long id;
    private String name;
    private String roadaddress;
    private String address;
    private String phone;
    private String imageUrl;
}
