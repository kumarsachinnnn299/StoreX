package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository< Product,Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
    /*
    Although these method findByCategoryOrderByPriceAsc,findByProductNameLikeIgnoreCase are not pre built
    methods in JPa but here these will create their SQl query by the name itself. So we don't have to write any SQL;

    We just have to declare them with a standard format name  since these methods are not provided by default
    */
}
