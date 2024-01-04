package com.wkdeveloper.multipleimage_uploaded;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.wkdeveloper.multipleimage_uploaded.Adapters.ImagePagerAdapter;
import com.wkdeveloper.multipleimage_uploaded.Models.ItemModel;
import com.wkdeveloper.multipleimage_uploaded.databinding.ActivityItemDetailsBinding;

import java.util.ArrayList;

public class ItemDetails extends AppCompatActivity {
    ItemModel itemModel;
    ViewPager detailViewPager;
    TextView detailItemNameTextView;
    TextView detailDescriptionTextView;
    ActivityItemDetailsBinding activityItemDetailsBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityItemDetailsBinding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityItemDetailsBinding.getRoot());

        initViews(activityItemDetailsBinding);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("data")) {
            itemModel = (ItemModel) intent.getParcelableExtra("data");
            if (itemModel != null) {
                // Proceed with using itemModel
                Toast.makeText(this, "Item data is available", Toast.LENGTH_SHORT).show();

                // Access the data from the ItemModel
                String itemName = itemModel.getItemName();
                String description = itemModel.getItemDesc();
                ArrayList<String> imageUrls = itemModel.getImageUrls();

                // Set item details to the views
                detailItemNameTextView.setText(itemName);
                detailDescriptionTextView.setText(description);

                // Set up the ViewPager with images
                ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, imageUrls);
                detailViewPager.setAdapter(pagerAdapter);
            } else {
                // Handle the case where itemModel is null
                Toast.makeText(this, "Item data is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initViews(ActivityItemDetailsBinding activityItemDetailsBinding) {
        detailViewPager = activityItemDetailsBinding.detailViewPager;
        detailItemNameTextView = activityItemDetailsBinding.detailItemNameTextView;
        detailDescriptionTextView = activityItemDetailsBinding.detailDescriptionTextView;
    }
}
