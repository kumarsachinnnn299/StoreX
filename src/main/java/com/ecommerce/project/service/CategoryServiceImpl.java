package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category>categoryPage=categoryRepository.findAll(pageDetails);
        List<Category>categories=categoryPage.getContent();
        if(categories.isEmpty())throw new APIException("No category available!! Kindly add a category.");
        List<CategoryDTO>categoryDTOS=categories.stream()
                .map(category->modelMapper.map(category,CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setLastPage(categoryPage.isLast());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category=modelMapper.map(categoryDTO,Category.class);
        Category categoryToSave=categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryToSave!=null)
            throw new APIException("Category with the name: "+ category.getCategoryName()+" already exist!!");
        Category savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

//        List<Category>categories=categoryRepository.findAll();
//        Category category=categories.stream()
//                .filter(c->c.getCategoryId().equals(categoryId))
//                .findFirst()
//                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));

//        if(category==null)return "Category not found!!";   -> This will not be required as we have used orElseThrow in above line
        categoryRepository.delete(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
//        List<Category>categories=categoryRepository.findAll();  -> Don't need to get all categories

        //just get the required category that needs to be updated
        Optional<Category> categoryToSaveOptional=categoryRepository.findById(categoryId);
        Category categoryToSave=categoryToSaveOptional
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        Category category=modelMapper.map(categoryDTO,Category.class);
        category.setCategoryId(categoryId);
        Category savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
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
