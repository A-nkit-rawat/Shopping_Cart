package com.ecom.service.impl;

import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.UserDtl;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;
import com.ecom.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Override
    public List<Cart> getCartByUserId(int userId) {

        List<Cart> listOfCarts=cartRepository.findCartByUserId(userId);

        return listOfCarts;
    }

    @Override
    public Integer countCartByUserId(int userId) {
        return cartRepository.countCartByUserId(userId);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;


    @Override
    public Cart getCartById(int id) {
        return cartRepository.findById(id).get();
    }

    @Override
    public Cart saveCart(int uid, int pid){
        Cart cart=cartRepository.findCartByUserIdAndProductId(uid, pid);
        if(ObjectUtils.isEmpty(cart)){
            UserDtl user=userRepository.findById(uid).get();
            Product product=productRepository.findById(pid).get();
            cart=new Cart();
            cart.setQuantity(1);
            cart.setUser(user);
            cart.setProduct(product);
//            cart.setPrice(product.getDiscountedPrice().longValue());

        }
        else{
            cart.setQuantity(cart.getQuantity()+1);
        }

        cartRepository.save(cart);
        return cart;
    }

    @Override
    public void deleteCart(Cart cart) {
        //id exist then delete
        cartRepository.delete(cart);

    }
    @Override
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
//        return flag;


    }






}
