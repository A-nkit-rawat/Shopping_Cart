package com.ecom.repository;

import com.ecom.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findCartByUserIdAndProductId(int userId,int productId);
    public Integer countCartByUserId(int userId);
    public List<Cart> findCartByUserId(int userId);

}
