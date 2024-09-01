package com.ecommerce.project.model.payload;
//This is a request object

import lombok.AllArgsConstructor;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;

}
