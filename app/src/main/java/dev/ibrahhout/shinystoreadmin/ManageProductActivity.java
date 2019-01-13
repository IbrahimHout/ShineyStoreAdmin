package dev.ibrahhout.shinystoreadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ibrahhout.shinystoreadmin.Adapters.ItemsAdapter;
import dev.ibrahhout.shinystoreadmin.Models.Product;
import dev.ibrahhout.shinystoreadmin.Utils.Constants;

import static android.content.ContentValues.TAG;

public class ManageProductActivity extends AppCompatActivity {

    @BindView(R.id.titleConfirm)
    TextView titleConfirm;
    @BindView(R.id.productsRecyclerView)
    RecyclerView productsRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.noItemsTV)
    TextView noItemsTV;
    @BindView(R.id.addProductFAB)
    FloatingActionButton addCatFAB;
    @BindView(R.id.addProduct)
    Button addProduct;
    @BindView(R.id.addProducts)
    Button addProducts;

    ArrayList<Product> products;
    ItemsAdapter itemsAdapter;
    String catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);
        ButterKnife.bind(this);


        catId = getIntent().getStringExtra(Constants.EXTRA_CATEGORY_ID);


        products = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(this,products,catId);
        productsRecyclerView.setAdapter(itemsAdapter);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        addCatFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageProductActivity.this,AddProductActivity.class);
                intent.putExtra(Constants.EXTRA_CATEGORY_ID,catId);
                startActivity(intent);
            }
        });



    }

    private void callItems(String catId) {

        FirebaseDatabase.getInstance().getReference().child(Constants.PRODUCTS).child(catId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        progressBar.setVisibility(View.VISIBLE);


                        products.clear();
                        for (DataSnapshot catSnap : dataSnapshot.getChildren()) {

                            Product cat = catSnap.getValue(Product.class);
                            cat.setId(catSnap.getKey());

                            Log.d(TAG, "onDataChange: ");

                            products.add(cat);
                        }


                        Collections.reverse(products);
                        updateAdapter(products);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ManageProductActivity.this, "Error Loading Data check your connection please,", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (catId!=null)
        callItems(catId);


    }



    private void updateAdapter(ArrayList<Product> products) {
        if(products.size()==0){
            progressBar.setVisibility(View.GONE);
            noItemsTV.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
            noItemsTV.setVisibility(View.GONE);


            itemsAdapter.notifyDataSetChanged();
        }
    }
}
