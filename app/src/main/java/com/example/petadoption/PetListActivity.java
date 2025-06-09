package com.example.petadoption;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PetListActivity extends AppCompatActivity {

    private RecyclerView recyclerPets;
    private DatabaseReference petsRef;
    private List<Pet> petList = new ArrayList<>();
    private PetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        recyclerPets = findViewById(R.id.recyclerPets);
        recyclerPets.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PetAdapter(petList, this);
        recyclerPets.setAdapter(adapter);

        petsRef = FirebaseDatabase.getInstance().getReference("pets");

        petsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                petList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Pet pet = ds.getValue(Pet.class);
                    petList.add(pet);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PetListActivity.this, "Erro ao carregar pets", Toast.LENGTH_SHORT).show();
            }
        });
    }
}