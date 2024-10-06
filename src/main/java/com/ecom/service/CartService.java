package com.ecom.service;

import com.ecom.model.Cart;

import java.util.List;

public interface CartService {

    public Cart getCartById(int id);
//    public Cart getCarByUserIdAndProductId(int userId, int productId);
    public Cart saveCart(int uid,int pid);
    public void saveCart(Cart cart);
    public Integer countCartByUserId(int userId);
    public List<Cart> getCartByUserId(int userId);
    public void deleteCart(Cart cart);
}
