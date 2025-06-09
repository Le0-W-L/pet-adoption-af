package com.example.petadoption;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private FirebaseAuth mAuth;

    // Adicione a variável para o novo botão
    private FloatingActionButton fabAddPet, fabLogout, fabMyAdoptions;

    private static final String TAG = "PetListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        // Encontre os 3 botões
        fabAddPet = findViewById(R.id.fab_add_pet);
        fabLogout = findViewById(R.id.fab_logout);
        fabMyAdoptions = findViewById(R.id.fab_my_adoptions); // Novo
        recyclerPets = findViewById(R.id.recyclerPets);

        recyclerPets.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PetAdapter(petList, this);
        recyclerPets.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        carregarPetsDisponiveis();

        // Ação do botão Adicionar
        fabAddPet.setOnClickListener(v -> startActivity(new Intent(this, AddPetActivity.class)));

        // Ação do botão Logout
        fabLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Ação do NOVO botão Minhas Adoções
        fabMyAdoptions.setOnClickListener(v -> {
            startActivity(new Intent(this, MyAdoptionsActivity.class));
        });
    }

    private void carregarPetsDisponiveis() {
        db.collection("pets")
                .whereEqualTo("adotado", false)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        Toast.makeText(PetListActivity.this, "Erro ao carregar pets. Verifique o Logcat.", Toast.LENGTH_SHORT).show();
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