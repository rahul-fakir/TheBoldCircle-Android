package com.rahulfakir.theboldcircle.ProductData;

import java.util.HashMap;

/**
 * Created by rahulfakir on 6/22/16.
 */
public class SecondLevelVariantObject {
    private String sku;
    private HashMap secondLevelVariantObject;

    public SecondLevelVariantObject(String sku, HashMap secondLevelVariantObject) {
        this.sku = sku;
        this.secondLevelVariantObject = secondLevelVariantObject;
    }


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public HashMap getSecondLevelVariantObject() {
        return secondLevelVariantObject;
    }

    public void setSecondLevelVariantObject(HashMap secondLevelVariantObject) {
        this.secondLevelVariantObject = secondLevelVariantObject;
    }
}