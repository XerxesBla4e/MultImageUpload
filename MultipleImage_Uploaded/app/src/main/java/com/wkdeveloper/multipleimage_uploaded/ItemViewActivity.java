package com.wkdeveloper.multipleimage_uploaded;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wkdeveloper.multipleimage_uploaded.Adapters.ItemAdapter;
import com.wkdeveloper.multipleimage_uploaded.Interface.OnItemClicked;
import com.wkdeveloper.multipleimage_uploaded.Models.ItemModel;
import com.wkdeveloper.multipleimage_uploaded.databinding.ItemViewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemViewActivity extends AppCompatActivity {
    ItemViewBinding itemViewBinding;
    RecyclerView recyclerView;

    List<ItemModel> arrayList;
    ItemAdapter itemAdapter;
    FirebaseFirestore firestore;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemViewBinding = ItemViewBinding.inflate(getLayoutInflater());
        setContentView(itemViewBinding.getRoot());

        initViews(itemViewBinding);
        shimmerFrameLayout.startShimmer();
        // Load and display items from Firestore
        loadItemsFromFirestore();

        itemAdapter.setOnItemClickedListener(new OnItemClicked() {
            @Override
            public void goToViewItemDetails(ItemModel itemModel) {
                Log.d("ItemModel", "ItemModel: " + itemModel.getItemDesc()+ "images: "+itemModel.getImageUrls()+"name: "+itemModel.getItemName()); // Log the itemModel
                Intent intent = new Intent(ItemViewActivity.this, ItemDetails.class);
                intent.putExtra("data", itemModel);
                startActivity(intent);
            }
        });
    }

    private void loadItemsFromFirestore() {
        firestore.collection("Items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            ItemModel item = document.toObject(ItemModel.class);
                            arrayList.add(item);
                        }
                        itemAdapter.setItemList(arrayList);
                        itemAdapter.notifyDataSetChanged();
                        updateUIAndStopShimmer();
                    } else {
                        // Handle failure
                        Toast.makeText(ItemViewActivity.this, "Failed to retrieve items", Toast.LENGTH_SHORT).show();
                        updateUIAndStopShimmer();
                    }
                });
    }


    public void initViews(ItemViewBinding itemViewBinding) {
        firestore = FirebaseFirestore.getInstance();
        recyclerView = itemViewBinding.recyclerViewItems;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
        // Initialize the adapter with an empty list
        itemAdapter = new ItemAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(itemAdapter);
        shimmerFrameLayout = itemViewBinding.shimmerLayout3;
    }

    private void updateUIAndStopShimmer() {
        // UI-related code here
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}
