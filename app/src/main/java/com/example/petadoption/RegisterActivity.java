package com.example.petadoption;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // Importar TextView
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText editNome, editEmail, editSenha;
    private Button btnRegistrar;
    private TextView tvFazerLogin; // Novo
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        tvFazerLogin = findViewById(R.id.tvFazerLogin); // Novo

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegistrar.setOnClickListener(v -> {
            // ... (código de registro existente)
            String nome = editNome.getText().toString();
            String email = editEmail.getText().toString();
            String senha = editSenha.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnSuccessListener(authResult -> {
                        String uid = mAuth.getCurrentUser().getUid();
                        Usuarios novoUsuario = new Usuarios(nome, email);
                        db.collection("usuarios").document(uid).set(novoUsuario)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show();
                                    // Alterado para ir para a lista de pets
                                    startActivity(new Intent(this, PetListActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Erro ao salvar dados do usuário.", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Erro ao registrar: " + e.getMessage(), Toast.LENGTH_LONG).show());
        });

        // Novo OnClickListener para o TextView
        tvFazerLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}