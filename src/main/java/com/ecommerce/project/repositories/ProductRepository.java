package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository< Product,Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);
    /*
    Although these method findByCategoryOrderByPriceAsc,findByProductNameLikeIgnoreCase are not pre built
    methods in JPa but here these will create their SQl query by the name itself. So we don't have to write any SQL;

    We just have to declare them with a standard format name  since these methods are not provided by default
    */
}
