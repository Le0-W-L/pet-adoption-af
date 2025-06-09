package com.example.petadoption;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyAdoptionsActivity extends AppCompatActivity {

    private RecyclerView recyclerMyPets;
    private FirebaseFirestore db;
    private List<Pet> myPetList = new ArrayList<>();
    private PetAdapter adapter;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyAdoptionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adoptions);

        Toolbar toolbar = findViewById(R.id.toolbar_my_adoptions);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerMyPets = findViewById(R.id.recyclerMyPets);
        recyclerMyPets.setLayoutManager(new LinearLayoutManager(this));
        // Reutilizamos o mesmo PetAdapter!
        adapter = new PetAdapter(myPetList, this);
        recyclerMyPets.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        carregarMeusPetsAdotados();
    }

    private void carregarMeusPetsAdotados() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuário não logado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();

        // AQUI ESTÁ A NOVA CONSULTA!
        db.collection("pets")
                .whereEqualTo("adotadoPorId", uid)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        Toast.makeText(this, "Erro ao carregar seus pets. Verifique o Logcat.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    myPetList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc != null) {
                            Pet pet = doc.toObject(Pet.class);
                            myPetList.add(pet);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}