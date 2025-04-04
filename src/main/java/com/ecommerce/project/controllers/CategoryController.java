package com.ecommerce.project.controllers;


import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")//this part will be common in all endpoints
@Tag(name="Category Controller", description = "This controller will " +
        "have all the endpoints for categories.")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Test endpoint for understanding how to use @RequestParan

//    @RequestMapping(value = "/public/echo",method = RequestMethod.GET)
//    @Operation(summary = "Just a test endpoint to know how to user @RequestParam")
//    public  ResponseEntity<String> echoMessage(@RequestParam(name="message", defaultValue = "Hello World!!!")String message )
//    {
//        return new ResponseEntity<>("Echoed Message: "+message,HttpStatus.OK);
//    }

    @GetMapping("/public/categories")
    @Operation(summary = "Get all categories.")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY,required = false)String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR,required = false)String sortOrder
    )
    {
        CategoryResponse categoryResponse =categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    @Operation(summary = "Create a new Category.")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO)//@Valid ensures Requestbody contains valid data
    {
        CategoryDTO savedCategoryDTO=categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    @Operation(summary = "Delete a category.")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
            CategoryDTO deletedCategoryDTO=categoryService.deleteCategory(categoryId);
            //3 ways to write
            return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);

    }


//    @PutMapping("/api/admin/categories/{categoryId}") this or RequestMapping are equivalent
    @RequestMapping(value = "/admin/categories/{categoryId}",method = RequestMethod.PUT)
    @Operation(summary = "Update a category.")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                 @PathVariable Long categoryId)
    {
                CategoryDTO updatedCategoryDTO=categoryService.updateCategory(categoryDTO,categoryId);
                return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }
}
