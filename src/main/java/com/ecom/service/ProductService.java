package com.ecom.service;

import com.ecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductService {
    public Product getProductByName(String name);
    public Product getProductById(int id);
    public List<Product> getAllProducts();
    public boolean saveProduct(Product product);
    public boolean exitsProductName(String name);
    public boolean exitsProductImageName(String image);
    public Product deleteProduct(int productId);
    public List<Product> getIsActiveProducts();
    public List<Product> getProductsByCategory(String category);

}
