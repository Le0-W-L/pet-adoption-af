package com.example.petadoption;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AddPetActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editNome, editIdade, editRaca;
    private ImageView imagePet;
    private Button btnEscolherImagem, btnCadastrar;

    private Uri imageUriSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        editNome = findViewById(R.id.editNomePet);
        editIdade = findViewById(R.id.editIdadePet);
        editRaca = findViewById(R.id.editRacaPet);
        imagePet = findViewById(R.id.imagePreview);
        btnEscolherImagem = findViewById(R.id.btnSelecionarImagem);
        btnCadastrar = findViewById(R.id.btnCadastrarPet);

        btnEscolherImagem.setOnClickListener(view -> escolherImagem());
        btnCadastrar.setOnClickListener(view -> cadastrarPet());
    }

    private void escolherImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUriSelecionada = data.getData();
            imagePet.setImageURI(imageUriSelecionada);
        }
    }

    private void cadastrarPet() {
        String nome = editNome.getText().toString().trim();
        String idadeStr = editIdade.getText().toString().trim();
        String raca = editRaca.getText().toString().trim();

        if (nome.isEmpty() || idadeStr.isEmpty() || raca.isEmpty() || imageUriSelecionada == null) {
            Toast.makeText(this, "Preencha todos os campos e escolha uma imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        int idade = Integer.parseInt(idadeStr);
        String donoId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference ref = FirebaseStorage.getInstance().getReference()
                .child("images_pets/" + UUID.randomUUID().toString());

        ref.putFile(imageUriSelecionada)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    Pet pet = new Pet(nome, idade, raca, imageUrl, donoId, false, "");

                    FirebaseDatabase.getInstance().getReference("pets")
                            .push()
                            .setValue(pet)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Pet cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, PetListActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Erro ao salvar no banco", Toast.LENGTH_SHORT).show());

                }).addOnFailureListener(e -> Toast.makeText(this, "Erro ao obter URL da imagem", Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Toast.makeText(this, "Falha ao subir imagem", Toast.LENGTH_SHORT).show());
    }
}