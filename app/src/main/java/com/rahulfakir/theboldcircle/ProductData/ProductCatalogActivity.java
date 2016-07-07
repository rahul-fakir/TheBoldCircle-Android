package com.rahulfakir.theboldcircle.ProductData;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rahulfakir.theboldcircle.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductCatalogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductCatalogAdapter pAdapter;
    public static List<ProductObject> productList = new ArrayList<>();
    private TextView tvSubmitProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_catalog);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        tvSubmitProducts = (TextView) findViewById(R.id.tvSubmitProducts);
        tvSubmitProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductCatalogActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });


        pAdapter = new ProductCatalogAdapter(productList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProductObject product = productList.get(position);
                System.out.println("sefghfhsdf" + position);
                Intent intent = new Intent(ProductCatalogActivity.this, ExpandedProductActivity.class);
                intent.putExtra("name", product.getName());
                intent.putExtra("description", product.getDescription());
                intent.putExtra("price", product.getBasePrice());
                intent.putExtra("brand", product.getBrand());
                intent.putExtra("index", position);

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareProductData();
    }

    private void prepareProductData() {
        findViewById(R.id.productsLoadingPanel).setVisibility(View.VISIBLE);
        productList.clear();
        pAdapter.notifyDataSetChanged();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = mDatabase.getReference("productData").child("products");

        productsRef.orderByChild("purchaseCount").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshotCategories : dataSnapshot.getChildren()) {
                            String category = postSnapshotCategories.getKey();
                            for (DataSnapshot postSnapshotBrands : postSnapshotCategories.getChildren()) {
                                String brand = postSnapshotBrands.getKey();
                                for (DataSnapshot postSnapshotProducts : postSnapshotBrands.getChildren()) {
                                    String sku = postSnapshotProducts.getKey();
                                    ProductObject product = new ProductObject();
                                    product.setType(Integer.valueOf(postSnapshotProducts.child("type").getValue().toString()));
                                    product.setName(postSnapshotProducts.child("name").getValue().toString());
                                    product.setDescription(postSnapshotProducts.child("description").getValue().toString());
                                    product.setBasePrice(Double.valueOf(postSnapshotProducts.child("basePrice").getValue().toString()));
                                    product.setBrand(brand);
                                    product.setCategory(category);
                                    product.setSku(sku);
                                    product.setVariantStatus(Boolean.valueOf(postSnapshotProducts.child("variantStatus").getValue().toString()));
                                    if (product.getType() == 0) {
                                        product.setStock(Integer.valueOf(postSnapshotProducts.child("stock").getValue().toString()));
                                    }

                                    String firstLevelValue, firstLevelSku, secondLevelSku, secondLevelValue;
                                    Double firstLevelPrice, secondLevelPrice;
                                    HashMap variants = new HashMap();

                                    int firstLevelStock, secondLevelStock;
                                    if (product.getVariantStatus()) {
                                        String firstVariantName = postSnapshotProducts.child("variants").child("firstName").getValue().toString();
                                        Boolean hasSecondVariantLevel = Boolean.valueOf(postSnapshotProducts.child("variants").child("hasSecondVariantLevel").getValue().toString());

                                        product.setFirstVariantLevelType(firstVariantName);
                                        product.setHasSecondVariantLevel(hasSecondVariantLevel);


                                        if (hasSecondVariantLevel) {
                                            product.setVariantDepth(2);
                                            String secondVariantName = postSnapshotProducts.child("variants").child("secondName").getValue().toString();
                                            product.setSecondVariantLevelType(secondVariantName);

                                            for (DataSnapshot postSnapshotFirstVariants : postSnapshotProducts.child("variants").child("firstVariantLevel").getChildren()) {
                                                firstLevelSku = postSnapshotFirstVariants.getKey();
                                                firstLevelValue = postSnapshotFirstVariants.child("value").getValue().toString();
                                                HashMap mapForSecondLevel = new HashMap();
                                                for (DataSnapshot postSnapshotSecondVariants : postSnapshotFirstVariants.child("secondVariantLevel").getChildren()) {

                                                    secondLevelSku = postSnapshotSecondVariants.getKey();
                                                    secondLevelValue = postSnapshotSecondVariants.child("name").getValue().toString();
                                                    secondLevelPrice = Double.valueOf(postSnapshotSecondVariants.child("price").getValue().toString());
                                                    secondLevelStock = Integer.valueOf(postSnapshotSecondVariants.child("stock").getValue().toString());
                                                    FirstLevelVariantObject secondLevelVariants = new FirstLevelVariantObject(secondLevelSku, secondLevelPrice, secondLevelStock);
                                                    mapForSecondLevel.put(secondLevelValue, secondLevelVariants);

                                                }
                                                SecondLevelVariantObject variant = new SecondLevelVariantObject(firstLevelSku, mapForSecondLevel);
                                                variants.put(firstLevelValue, variant);

                                            }
                                            product.setVariants(variants);
                                        } else {
                                            product.setVariantDepth(1);
                                            for (DataSnapshot postSnapshotFirstVariants : postSnapshotProducts.child("variants").child("firstVariantLevel").getChildren()) {
                                                firstLevelSku = postSnapshotFirstVariants.getKey();
                                                firstLevelValue = postSnapshotFirstVariants.child("value").getValue().toString();
                                                firstLevelStock = Integer.valueOf(postSnapshotFirstVariants.child("stock").getValue().toString());
                                                firstLevelPrice = Double.valueOf(postSnapshotFirstVariants.child("price").getValue().toString());
                                                FirstLevelVariantObject variant = new FirstLevelVariantObject(firstLevelSku, firstLevelPrice, firstLevelStock);
                                                variants.put(firstLevelValue, variant);

                                            }

                                            product.setVariants(variants);
                                        }
                                    }

                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReferenceFromUrl("gs://project-1889047848887901903.appspot.com/" + product.getSku() + "-thumbnail.jpg");


                                    File localFile = null;
                                    try {
                                        localFile = File.createTempFile(product.getSku() + "-thumbnail", "jpg");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            // Local temp file has been created
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });






                                    productList.add(product);


                                    System.out.println("Name " + product.getName());
                                    System.out.println("Brand " + product.getBrand());
                                    System.out.println("Price " + product.getBasePrice());
                                    System.out.println("Description " + product.getDescription());
                                    System.out.println("VariantStatus " + product.getVariantStatus());
                                    System.out.println("Type " + product.getType());
                                    System.out.println("VariantDepth " + product.getVariantDepth());
                                    if (product.getVariantStatus()) {
                                        System.out.println("FirstVariantKind " + product.getFirstVariantLevelType());
                                        System.out.println("HasSecondVariantLevel " + product.getHasSecondVariantLevel());
                                        if (product.getHasSecondVariantLevel()) {

                                        } else {
                                            System.out.println("Number of " + product.getFirstVariantLevelType() + "s to choose from");
                                            System.out.println(product.getVariants().size());
                                            HashMap<String, FirstLevelVariantObject> test = product.getVariants();
                                            for (Map.Entry<String, FirstLevelVariantObject> studentEntry : test.entrySet()) {

                                                System.out.println(studentEntry.getKey());

                                                //if you uncomment below code, it will throw java.util.ConcurrentModificationException
                                                //studentGrades.remove("Alan");
                                            }

                                        }
                                    }
                                }

                            }
                        }

                        pAdapter.notifyDataSetChanged();
                        findViewById(R.id.productsLoadingPanel).setVisibility(View.GONE);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ProductCatalogActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ProductCatalogActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
