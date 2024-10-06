package com.ecom.Utility;

public enum OrderStatus {

    IN_PROGRESS(1,"In Progress"),
    ORDER_RECEIVED(2,"Order Received"),
    PRODUCT_PACKED(3,"Product Packed"),
    OUT_OF_DELIVERY(4,"Out Of Delivery"),
    DELIVERED(5,"Delivered"),
    CANCEL(6,"Cancelled");

    private int id;
    private String currentStatus;

    OrderStatus() {
    }

    OrderStatus(int id, String currentStatus) {
        this.id = id;
        this.currentStatus = currentStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
