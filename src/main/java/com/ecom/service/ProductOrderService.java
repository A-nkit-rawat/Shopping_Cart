package com.ecom.service;

import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import org.hibernate.query.Order;

import java.util.List;

public interface ProductOrderService {
    public void saveProductOrder(int userId, OrderRequest orderRequest);
    public List<ProductOrder> getProductOrdersByUserId(int userId);
    public ProductOrder getProductOrderByProductOrderId(int productOrderId);
    public void updateProductOrder(ProductOrder productOrder);
    public List<ProductOrder> getAllProductOrders();
}
