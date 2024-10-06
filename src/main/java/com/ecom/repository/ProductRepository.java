package com.ecom.repository;

import com.ecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    public boolean existsByName(String name);
    public Product findByName(String name);
    public boolean existsByImage(String image);
    public List<Product> findByIsActiveTrue();
    public List<Product> findByCategory(String categoryName);

}
