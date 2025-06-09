package com.example.petadoption;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PetListActivity extends AppCompatActivity {

    private RecyclerView recyclerPets;
    private FirebaseFirestore db;
    private List<Pet> petList = new ArrayList<>();
    private PetAdapter adapter;
    private FirebaseAuth mAuth; // Novo

    private FloatingActionButton fabAddPet, fabLogout; // Novos

    private static final String TAG = "PetListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        mAuth = FirebaseAuth.getInstance(); // Novo

        fabAddPet = findViewById(R.id.fab_add_pet);
        fabLogout = findViewById(R.id.fab_logout);
        recyclerPets = findViewById(R.id.recyclerPets);

        recyclerPets.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PetAdapter(petList, this);
        recyclerPets.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        carregarPets(); // Chamando método separado

        // Ação do botão Adicionar
        fabAddPet.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPetActivity.class));
        });

        // Ação do botão Logout
        fabLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            // Limpa o histórico de telas para que o usuário não possa "voltar" para a tela de lista
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void carregarPets() {
        db.collection("pets").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                Toast.makeText(PetListActivity.this, "Erro ao carregar pets", Toast.LENGTH_SHORT).show();
                return;
            }

            petList.clear();
            for (QueryDocumentSnapshot doc : value) {
                if (doc != null) {
                    Pet pet = doc.toObject(Pet.class);
                    petList.add(pet);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }
}