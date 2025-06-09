package com.example.petadoption;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PetDetailActivity extends AppCompatActivity {

    private ImageView imagePetDetail;
    private TextView tvPetNameDetail, tvPetInfoDetail, tvStatusAdocao;
    private Button btnAdotar;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String petId;
    private DocumentReference petRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        // Inicializa os componentes do layout
        imagePetDetail = findViewById(R.id.imagePetDetail);
        tvPetNameDetail = findViewById(R.id.tvPetNameDetail);
        tvPetInfoDetail = findViewById(R.id.tvPetInfoDetail);
        tvStatusAdocao = findViewById(R.id.tvStatusAdocao);
        btnAdotar = findViewById(R.id.btnAdotar);

        // Inicializa o Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Pega o ID do pet passado pela Intent
        petId = getIntent().getStringExtra("PET_ID");

        if (petId == null) {
            Toast.makeText(this, "Erro: Pet não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Referência ao documento do pet no Firestore
        petRef = db.collection("pets").document(petId);

        carregarDadosDoPet();

        btnAdotar.setOnClickListener(v -> adotarPet());
    }

    private void carregarDadosDoPet() {
        petRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Pet pet = documentSnapshot.toObject(Pet.class);
                if (pet != null) {
                    // Preenche os dados na tela
                    tvPetNameDetail.setText(pet.getNome());
                    String info = "Raça: " + pet.getRaca() + ", Idade: " + pet.getIdade() + " anos";
                    tvPetInfoDetail.setText(info);
                    Glide.with(this).load(pet.getImagemUrl()).into(imagePetDetail);

                    // Verifica o status da adoção e quem está vendo a página
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (pet.isAdotado()) {
                        btnAdotar.setVisibility(View.GONE);
                        tvStatusAdocao.setVisibility(View.VISIBLE);
                    } else if (currentUser != null && currentUser.getUid().equals(pet.getDonoId())) {
                        // Se o usuário logado for o dono, esconde o botão
                        btnAdotar.setVisibility(View.GONE);
                    } else {
                        btnAdotar.setVisibility(View.VISIBLE);
                        tvStatusAdocao.setVisibility(View.GONE);
                    }
                }
            } else {
                Toast.makeText(this, "Pet não encontrado.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show();
        });
    }

    private void adotarPet() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Você precisa estar logado para adotar.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        // Atualiza os campos 'adotado' e 'adotadoPorId' no Firestore
        petRef.update("adotado", true, "adotadoPorId", userId)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Parabéns, você adotou este pet!", Toast.LENGTH_LONG).show();
                    // Atualiza a UI para refletir a adoção
                    btnAdotar.setVisibility(View.GONE);
                    tvStatusAdocao.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao tentar adotar. Tente novamente.", Toast.LENGTH_SHORT).show();
                });
    }
}