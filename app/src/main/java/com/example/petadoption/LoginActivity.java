package com.example.petadoption;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Button btnLogin;
    private TextView tvCadastro;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnLogin = findViewById(R.id.btnLogin);
        tvCadastro = findViewById(R.id.tvCadastro);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString();
            String senha = editSenha.getText().toString();

            if (!email.isEmpty() && !senha.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, senha)
                        .addOnSuccessListener(authResult -> {
                            startActivity(new Intent(this, AddPetActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Erro no login: " + e, Toast.LENGTH_LONG).show());
            }
        });

        tvCadastro.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}