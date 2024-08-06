package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        long count=categoryRepository.count();
        if(count==0)throw new APIException("No category available!! Kindly add a category.");
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory=categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory!=null)
            throw new APIException("Category with the name: "+ category.getCategoryName()+" already exist!!");
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));



//        List<Category>categories=categoryRepository.findAll();
//        Category category=categories.stream()
//                .filter(c->c.getCategoryId().equals(categoryId))
//                .findFirst()
//                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));

//        if(category==null)return "Category not found!!";   -> This will not be required as we have used orElseThrow in above line
        categoryRepository.delete(category);
        return "Category with Category ID: "+categoryId+" is deleted successfully!!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
//        List<Category>categories=categoryRepository.findAll();  -> Don't need to get all categories

        //just get the required category that needs to be updated
        Optional<Category> savedCategoryOptional=categoryRepository.findById(categoryId);
        Category savedCategory=savedCategoryOptional
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        category.setCategoryId(categoryId);
        savedCategory=categoryRepository.save(category);
        return savedCategory;
        //The commented cade was used initially but yhe non commented code is more optimised
//        Optional<Category> optionalCategory=categories.stream()
//                .filter(c->c.getCategoryId().equals(categoryId))
//                .findFirst();
//        if(optionalCategory.isPresent())
//        {
//            Category exisitingCategory=optionalCategory.get();
//            exisitingCategory.setCategoryName(category.getCategoryName());
//            Category savedCategory=categoryRepository.save(exisitingCategory);
//            return savedCategory;
//        }
//        else{
//            throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found!!");
//        }


    }


}
