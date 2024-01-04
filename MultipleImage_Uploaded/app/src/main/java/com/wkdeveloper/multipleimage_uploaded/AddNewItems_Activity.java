package com.wkdeveloper.multipleimage_uploaded;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wkdeveloper.multipleimage_uploaded.Adapters.ViewPagerAdapter;
import com.wkdeveloper.multipleimage_uploaded.Models.ItemModel;

import java.util.ArrayList;

public class AddNewItems_Activity extends AppCompatActivity {
    AppCompatButton Uploadbutton, viewItems;
    TextInputEditText ItemName, ItemDesc;
    RelativeLayout PickImagebutton;
    ViewPager viewPager;
    Uri ImageUri;
    ArrayList<Uri> ChooseImageList;
    ArrayList<String> UrlsList;
    FirebaseFirestore firestore;
    StorageReference storagereference;
    FirebaseStorage mStorage;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_items);
        PickImagebutton = findViewById(R.id.ChooseImage);
        viewPager = findViewById(R.id.viewPager);
        ItemDesc = findViewById(R.id.ItemDesc);
        ItemName = findViewById(R.id.ItemName);
        viewItems = findViewById(R.id.viewitems);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Data");
        progressDialog.setMessage("Please Wait While Uploading Your data...");


        // firebase Instance
        firestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        storagereference = mStorage.getReference();
        Uploadbutton = findViewById(R.id.UploadBtn);

        ChooseImageList = new ArrayList<>();
        UrlsList = new ArrayList<>();
        PickImagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckPermission();

            }
        });
        Uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadIMages();
            }
        });
        viewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewItems_Activity.this, ItemViewActivity.class));
            }
        });
    }

    private void UploadIMages() {
        final String timestamp = String.valueOf(System.currentTimeMillis());
        // we need list that images urls
        for (int i = 0; i < ChooseImageList.size(); i++) {
            Uri IndividualImage = ChooseImageList.get(i);
            if (IndividualImage != null) {
                progressDialog.show();
                StorageReference filepath = FirebaseStorage.getInstance().getReference().child("imagePost").child(timestamp);
                final StorageReference ImageName = filepath.child("Image" + i + ": " + IndividualImage.getLastPathSegment());
                ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UrlsList.add(String.valueOf(uri));
                                if (UrlsList.size() == ChooseImageList.size()) {
                                    StoreLinks(UrlsList, timestamp);
                                }
                            }
                        });

                    }
                });
            } else {
                Toast.makeText(this, "Please fill All Field", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void StoreLinks(ArrayList<String> urlsList, String timestamp) {
        // now we need get text from EditText
        String Name = ItemName.getText().toString();
        String Description = ItemDesc.getText().toString();

        if (!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Description) && ImageUri != null) {
            // Now we need a model class
            ItemModel model = new ItemModel(Name, Description, timestamp, urlsList);

            firestore.collection("Items").document(timestamp).set(model)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(AddNewItems_Activity.this, "Your data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        ChooseImageList.clear();

    }


    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddNewItems_Activity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddNewItems_Activity.this, new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            } else {
                PickImageFromgallry();
            }

        } else {
            PickImageFromgallry();
        }
    }

    private void PickImageFromgallry() {
        // here we go to gallery and select Image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i++) {
                ImageUri = data.getClipData().getItemAt(i).getUri();
                ChooseImageList.add(ImageUri);
                // now we need Adapter to show Images in viewpager
            }
            setAdapter();

        }
    }

    private void setAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, ChooseImageList);
        viewPager.setAdapter(adapter);
    }
}





