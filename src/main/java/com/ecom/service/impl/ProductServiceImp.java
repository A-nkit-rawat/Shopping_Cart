package com.ecom.service.impl;

import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {


    @Autowired
    ProductRepository productRepository;

    @Override
    public Product getProductByName(String name) {
        if(name==null){
            return null;
        }

        return productRepository.findByName(name);
    }

    @Override
    public Product getProductById(int id) {
        if(id==0){
            return null;
        }
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public boolean  saveProduct(Product product)  {
        if(product==null){
            return false;
        }
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean exitsProductName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public boolean exitsProductImageName(String image) {
        return productRepository.existsByImage(image);
    }

    @Override
    public Product deleteProduct(int productId) {
        if(productId==0){
            return null;
        }
        Product deleteproduct=productRepository.findById(productId).orElse(null);
        productRepository.delete(deleteproduct);
        return deleteproduct;
    }
    @Override
    public List<Product> getIsActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName){
        return productRepository.findByCategory(categoryName);
    }

}
