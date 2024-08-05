package com.ecommerce.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity(name="categories")//this input value will be the name of the table created in database
@Data
@NoArgsConstructor//this is from lombork ->  will create no args constructor at compile time and similarly read others
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;
    @NotBlank
    @Size(min=5,message = "Category name must contain atleast 5 characters")
    private String categoryName;

}
