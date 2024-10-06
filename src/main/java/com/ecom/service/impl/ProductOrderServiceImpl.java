package com.ecom.service.impl;

import com.ecom.Utility.OrderStatus;
import com.ecom.model.Cart;
import com.ecom.model.OrderAddress;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductOrderRepository;
import com.ecom.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {


    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private CartRepository cartRepository;


    @Override
    public void saveProductOrder(int userId, OrderRequest orderRequest) {
        List<Cart> listOfCarts= cartRepository.findCartByUserId(userId);
        for(Cart cart:listOfCarts){
            ProductOrder order= new ProductOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setProduct(cart.getProduct());
            order.setUser(cart.getUser());
            order.setPaymentType(orderRequest.getPaymentType());
            order.setQuantity(cart.getQuantity());
            order.setPrice(cart.getProduct().getDiscountedPrice().longValue());
            order.setOrderDate(LocalDate.now());
            order.setStatus(OrderStatus.IN_PROGRESS.getCurrentStatus());
            order.setOrderAddress(getOrderAddress(orderRequest));

            productOrderRepository.save(order);

        }
    }


    public OrderAddress getOrderAddress(OrderRequest orderRequest){
        if(ObjectUtils.isEmpty(orderRequest)){
            return null;
        }
        OrderAddress address=new OrderAddress();
        address.setFirstName(orderRequest.getFirstName());
        address.setLastName(orderRequest.getLastName());
        address.setAddress(orderRequest.getAddress());
        address.setState(orderRequest.getState());
        address.setCity(orderRequest.getCity());
        address.setEmail(orderRequest.getEmail());
        address.setPincode(orderRequest.getPincode());
        address.setMobileNo(orderRequest.getMobileNo());
        return address;
    }

    @Override
    public List<ProductOrder> getProductOrdersByUserId(int userId) {

        List<ProductOrder> listOfProductOrder=productOrderRepository.findAll();
        return listOfProductOrder;
    }

    @Override
    public ProductOrder getProductOrderByProductOrderId(int productOrderId) {
        ProductOrder productOrder=productOrderRepository.findById(productOrderId).get();
        return productOrder;
    }

    @Override
    public void updateProductOrder(ProductOrder productOrder) {
        if(ObjectUtils.isEmpty(productOrder)){

        }
        productOrderRepository.save(productOrder);
    }

    @Override
    public List<ProductOrder> getAllProductOrders() {
        return productOrderRepository.findAll();
    }
}
