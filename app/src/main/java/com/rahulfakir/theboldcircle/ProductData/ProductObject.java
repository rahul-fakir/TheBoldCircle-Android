package com.rahulfakir.theboldcircle.ProductData;

import java.util.HashMap;

public class ProductObject {
    private String category, sku, brand, description, name, firstVariantLevelType, secondVariantLevelType;
    private Double basePrice;
    private Boolean priceIsVariantDependent, variantStatus, hasSecondVariantLevel;
    private int type, stock = 0, variantDepth = 0;
    private HashMap variants = new HashMap();


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Boolean getPriceIsVariantDependent() {
        return priceIsVariantDependent;
    }

    public void setPriceIsVariantDependent(Boolean priceIsVariantDependent) {
        this.priceIsVariantDependent = priceIsVariantDependent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Boolean getVariantStatus() {
        return variantStatus;
    }

    public void setVariantStatus(Boolean variantStatus) {
        this.variantStatus = variantStatus;
    }

    public int getStock() {
        return stock;
    }


    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getFirstVariantLevelType() {
        return firstVariantLevelType;
    }

    public String getSecondVariantLevelType() {
        return secondVariantLevelType;
    }

    public void setSecondVariantLevelType(String secondVariantLevelType) {
        this.secondVariantLevelType = secondVariantLevelType;
    }

    public Boolean getHasSecondVariantLevel() {
        return hasSecondVariantLevel;
    }

    public void setHasSecondVariantLevel(Boolean hasSecondVariantLevel) {
        this.hasSecondVariantLevel = hasSecondVariantLevel;
    }

    public void setFirstVariantLevelType(String firstVariantLevelType) {
        this.firstVariantLevelType = firstVariantLevelType;
    }

    public int getVariantDepth() {
        return variantDepth;
    }

    public void setVariantDepth(int variantDepth) {
        this.variantDepth = variantDepth;
    }


    public HashMap getVariants() {
        return variants;
    }

    public void setVariants(HashMap variants) {
        this.variants = variants;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
