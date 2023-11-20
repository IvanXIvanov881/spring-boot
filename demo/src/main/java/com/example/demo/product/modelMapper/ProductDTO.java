package com.example.demo.product.modelMapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductDTO {


    private String name;
    private String description;
    private double price;
    private String unit;

}