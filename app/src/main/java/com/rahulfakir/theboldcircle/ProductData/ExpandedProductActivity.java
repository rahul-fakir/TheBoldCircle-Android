package com.rahulfakir.theboldcircle.ProductData;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.rahulfakir.theboldcircle.HomeScreenActivity;
import com.rahulfakir.theboldcircle.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpandedProductActivity extends AppCompatActivity {
    private TextView tvName, tvPrice, tvBrand, tvDescription, tvFirstOptionKind, tvSecondOptionKind;
    private Button btnAddToCart;
    private Spinner spnFirstOption, spnSecondOption;
    private ArrayList<String> firstOptionsList = new ArrayList<>();
    private ArrayList<String> secondOptionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_product);

        tvName = (TextView) findViewById(R.id.tvName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        spnFirstOption = (Spinner) findViewById(R.id.spnFirstOption);
        spnSecondOption = (Spinner) findViewById(R.id.spnSecondOption);
        tvFirstOptionKind = (TextView) findViewById(R.id.tvFirstOption);
        tvSecondOptionKind = (TextView) findViewById(R.id.tvSecondOption);


        Intent intent = getIntent();
        final int index = intent.getIntExtra("index", -1);
        tvName.setText(ProductCatalogActivity.productList.get(index).getName());
        tvBrand.setText("by " + ProductCatalogActivity.productList.get(index).getBrand());
        tvDescription.setText(ProductCatalogActivity.productList.get(index).getDescription());
        tvPrice.setText("$" + ProductCatalogActivity.productList.get(index).getBasePrice());


        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = ProductCatalogActivity.productList.get(index).getBrand();
                int variantDepth = ProductCatalogActivity.productList.get(index).getVariantDepth();
                String sku = ProductCatalogActivity.productList.get(index).getSku();
                String category = ProductCatalogActivity.productList.get(index).getCategory();
                String firstLevelSku = "";
                String secondLevelSku = "";
                System.out.println(sku);
                if (variantDepth == 1) {
                    FirstLevelVariantObject variant = (FirstLevelVariantObject) ProductCatalogActivity.productList.get(index).getVariants().get(spnFirstOption.getSelectedItem().toString());
                    firstLevelSku = variant.getSku();


                } else if (variantDepth == 2) {

                    SecondLevelVariantObject variant = (SecondLevelVariantObject) ProductCatalogActivity.productList.get(index).getVariants().get(spnFirstOption.getSelectedItem().toString());
                    firstLevelSku = variant.getSku();
                    HashMap<String, FirstLevelVariantObject> secondVariant = variant.getSecondLevelVariantObject();
                    secondLevelSku = "/" + secondVariant.get(spnSecondOption.getSelectedItem().toString()).getSku();

                }

                String productPath = "productData/products/" + category + "/" + brand + "/" + sku + "~" + firstLevelSku + secondLevelSku;
                if (HomeScreenActivity.cart.containsKey(productPath)) {
                    HomeScreenActivity.cart.put(productPath, HomeScreenActivity.cart.get(productPath) + 1);
                } else {
                    HomeScreenActivity.cart.put(productPath, 1);
                }


            }
        });

        spnFirstOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (ProductCatalogActivity.productList.get(index).getVariantDepth() == 1) {
                    FirstLevelVariantObject option = (FirstLevelVariantObject) ProductCatalogActivity.productList.get(index).getVariants().get(spnFirstOption.getItemAtPosition(position));
                    tvPrice.setText("$" + option.getPrice());
                } else {
                    secondOptionsList.clear();
                    SecondLevelVariantObject option = (SecondLevelVariantObject) ProductCatalogActivity.productList.get(index).getVariants().get(spnFirstOption.getItemAtPosition(position));
                    System.out.println(option.getSecondLevelVariantObject().size() + "");
                    HashMap<String, FirstLevelVariantObject> secondOptions = option.getSecondLevelVariantObject();
                    for (Map.Entry<String, FirstLevelVariantObject> secondOption : secondOptions.entrySet()) {
                        secondOptionsList.add(secondOption.getKey());
                        tvPrice.setText("$" + secondOption.getValue().getPrice());
                    }
                    ArrayAdapter<String> stringArrayAdapter =
                            new ArrayAdapter<String>(ExpandedProductActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    secondOptionsList);
                    spnSecondOption.setAdapter(stringArrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spnSecondOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                SecondLevelVariantObject option = (SecondLevelVariantObject) ProductCatalogActivity.productList.get(index).getVariants().get(spnFirstOption.getSelectedItem());
                FirstLevelVariantObject secondOptions = (FirstLevelVariantObject) option.getSecondLevelVariantObject().get(spnSecondOption.getSelectedItem());
                tvPrice.setText("$" + secondOptions.getPrice());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        if (ProductCatalogActivity.productList.get(index).getVariantStatus()) {
            tvFirstOptionKind.setText("Select a " + ProductCatalogActivity.productList.get(index).getFirstVariantLevelType());

            if (ProductCatalogActivity.productList.get(index).getVariantDepth() == 1) {
                HashMap<String, FirstLevelVariantObject> firstOptions = ProductCatalogActivity.productList.get(index).getVariants();
                for (Map.Entry<String, FirstLevelVariantObject> option : firstOptions.entrySet()) {
                    firstOptionsList.add(option.getKey());
                    tvPrice.setText("$" + option.getValue().getPrice());
                }
                ArrayAdapter<String> stringArrayAdapter =
                        new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_dropdown_item,
                                firstOptionsList);
                spnFirstOption.setAdapter(stringArrayAdapter);
                tvSecondOptionKind.setVisibility(View.GONE);
                spnSecondOption.setVisibility(View.GONE);
            } else {
                tvSecondOptionKind.setText("Select a " + ProductCatalogActivity.productList.get(index).getSecondVariantLevelType());
                HashMap<String, SecondLevelVariantObject> firstOptions = ProductCatalogActivity.productList.get(index).getVariants();
                Boolean firstRun = true;
                for (Map.Entry<String, SecondLevelVariantObject> option : firstOptions.entrySet()) {
                    firstOptionsList.add(option.getKey());
                    if (firstRun) {
                        HashMap<String, FirstLevelVariantObject> secondOptions = option.getValue().getSecondLevelVariantObject();
                        for (Map.Entry<String, FirstLevelVariantObject> secondOption : secondOptions.entrySet()) {
                            secondOptionsList.add(secondOption.getKey());
                            tvPrice.setText("$" + secondOption.getValue().getPrice());
                        }
                        ArrayAdapter<String> stringArrayAdapter =
                                new ArrayAdapter<String>(this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        secondOptionsList);
                        spnSecondOption.setAdapter(stringArrayAdapter);
                    }
                    firstRun = false;
                }
                ArrayAdapter<String> stringArrayAdapter =
                        new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_dropdown_item,
                                firstOptionsList);

                spnFirstOption.setAdapter(stringArrayAdapter);

            }


        } else {
            tvFirstOptionKind.setVisibility(View.GONE);
            spnFirstOption.setVisibility(View.GONE);
            tvSecondOptionKind.setVisibility(View.GONE);
            spnSecondOption.setVisibility(View.GONE);
        }


    }
}