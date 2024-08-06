package com.ecommerce.project.controllers;


import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")//this part will be common in all endpoints
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories()
    {
        CategoryResponse categoryResponse =categoryService.getAllCategories();
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category)//@Valid ensures Requestbody contains valid data
    {   categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully!!!", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable Long categoryId){
            String status=categoryService.deleteCategory(categoryId);
            //3 ways to write
            return new ResponseEntity<>(status, HttpStatus.OK);
//            return ResponseEntity.ok(status);
//            return ResponseEntity.status(ok).body(status);
    }

//    @PutMapping("/api/admin/categories/{categoryId}") this or RequestMapping are equivalent
    @RequestMapping(value = "/admin/categories/{categoryId}",method = RequestMethod.PUT)
    public ResponseEntity<String> updateCategory(@Valid @RequestBody Category category,
                                                 @PathVariable Long categoryId)
    {
                Category savedCategory=categoryService.updateCategory(category,categoryId);
                return new ResponseEntity<>("Category with category id "+categoryId+" is updated successfully!!", HttpStatus.OK);
    }
}
