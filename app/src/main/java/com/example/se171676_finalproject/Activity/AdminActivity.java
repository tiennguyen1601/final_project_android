// AdminActivity.java
package com.example.se171676_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.se171676_finalproject.Adapter.ItemsAdapter;
import com.example.se171676_finalproject.Domain.ItemsDomain;
import com.example.se171676_finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ItemsAdapter itemsAdapter;
    private ArrayList<ItemsDomain> itemsList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        itemsList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(itemsList, this);
        recyclerViewProducts.setAdapter(itemsAdapter);

        loadItems();

        findViewById(R.id.addProductButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddItemActivity.class);
                startActivity(intent); // Mở AddItemActivity
            }
        });

        findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        itemsAdapter.setOnItemClickListener(new ItemsAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
            }

            @Override
            public void onDeleteClick(int position) {
                deleteItem(position);
            }
        });
    }
    private void logout() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    private void loadItems() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemsDomain item = snapshot.getValue(ItemsDomain.class);
                    if (item != null) {
                        item.setId(snapshot.getKey());
                        itemsList.add(item);
                    }
                }
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Failed to load items.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem(int position) {
        if (position < 0 || position >= itemsList.size()) {
            Toast.makeText(AdminActivity.this, "Invalid item position", Toast.LENGTH_SHORT).show();
            return;
        }

        ItemsDomain itemToDelete = itemsList.get(position);

        if (itemToDelete.getId() != null) {
            databaseReference.child(itemToDelete.getId()).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Double-check that the position is still valid before removing
                    if (position < itemsList.size()) {
                        itemsList.remove(position);
                        itemsAdapter.notifyItemRemoved(position);
                    }
                    Toast.makeText(AdminActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Item ID is null", Toast.LENGTH_SHORT).show();
        }
    }


}
