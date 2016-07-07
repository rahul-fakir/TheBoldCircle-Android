package com.rahulfakir.theboldcircle.ProductData;

/**
 * Created by rahulfakir on 6/22/16.
 */

public class FirstLevelVariantObject {
    private String sku;
    private Double price;
    private int stock;


    public FirstLevelVariantObject(String sku, Double price, int stock) {

        this.sku = sku;
        this.price = price;
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}