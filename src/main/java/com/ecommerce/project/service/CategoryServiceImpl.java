package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
//    private List<Category> categories=new ArrayList<>();
    private Long nextId=1L;

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category>categories=categoryRepository.findAll();
        Category category=categories.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));

//        if(category==null)return "Category not found!!";   -> This will not be required as we have used orElseThrow in above line
        categoryRepository.delete(category);
        return "Category with Category ID: "+categoryId+" is deleted successfully!!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        List<Category>categories=categoryRepository.findAll();
        Optional<Category> optionalCategory=categories.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst();
        if(optionalCategory.isPresent())
        {
            Category exisitingCategory=optionalCategory.get();
            exisitingCategory.setCategoryName(category.getCategoryName());
            Category savedCategory=categoryRepository.save(exisitingCategory);
            return savedCategory;
        }
        else{
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found!!");
        }


    }


}
