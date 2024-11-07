package com.example.se171676_finalproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.se171676_finalproject.Domain.ItemsDomain;
import com.example.se171676_finalproject.R;
import com.example.se171676_finalproject.databinding.ActivityAddItemBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {
    ActivityAddItemBinding binding;
    private EditText itemTitle, itemDescription, itemPrice, itemOldPrice, itemRating, itemReview;
    Uri imgUrl;
    private Button addButton;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ArrayList<String> imageUrls = new ArrayList<>(); // Tạo danh sách để lưu URL ảnh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        itemTitle = findViewById(R.id.item_title);
        itemDescription = findViewById(R.id.item_description);
        itemPrice = findViewById(R.id.item_price);
        itemOldPrice = findViewById(R.id.item_oldPrice);
        itemRating = findViewById(R.id.item_rating);
        itemReview = findViewById(R.id.item_review);
        addButton = findViewById(R.id.add_button);
        storageReference = FirebaseStorage.getInstance().getReference("ItemImages");
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        binding.selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUrl != null) {
                    uploadImage();
                } else {
                    Toast.makeText(AddItemActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImage() {
        String fileName = "image_" + System.currentTimeMillis();
        StorageReference fileRef = storageReference.child(fileName);

        // Tải ảnh lên Firebase Storage
        fileRef.putFile(imgUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Lấy URL ảnh sau khi tải lên thành công
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    imageUrls.add(imageUrl); // Thêm URL vào danh sách
                    addItem(); // Thêm sản phẩm sau khi đã có URL ảnh
                });
            } else {
                Toast.makeText(AddItemActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void addItem() {
        String title = itemTitle.getText().toString().trim();
        String description = itemDescription.getText().toString().trim();
        String priceStr = itemPrice.getText().toString().trim();
        String oldPriceStr = itemOldPrice.getText().toString().trim();
        String ratingStr = itemRating.getText().toString().trim();
        String reviewStr = itemReview.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        double oldPrice = Double.parseDouble(oldPriceStr);
        double rating = ratingStr.isEmpty() ? 0 : Double.parseDouble(ratingStr);
        int review = reviewStr.isEmpty() ? 0 : Integer.parseInt(reviewStr);

        String id = databaseReference.push().getKey();
        ItemsDomain newItem = new ItemsDomain(id, title, description, price, oldPrice, rating, review, imageUrls);

        databaseReference.child(id).setValue(newItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddItemActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddItemActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUrl = data.getData();
            binding.firebaseImage.setImageURI(imgUrl);
        }
    }
}
