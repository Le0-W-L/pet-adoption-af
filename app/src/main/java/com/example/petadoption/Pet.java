package com.example.petadoption;

import com.google.firebase.firestore.DocumentId;

public class Pet {

    @DocumentId // Anotação importante do Firestore
    private String id;

    private String nome;
    private int idade;
    private String raca;
    private String donoId;
    private boolean adotado;
    private String adotadoPorId;
    private String imagemUrl;

    // Construtor vazio é obrigatório para o Firestore
    public Pet() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getAdotadoPorId() {
        return adotadoPorId;
    }

    public void setAdotadoPorId(String adotadoPorId) {
        this.adotadoPorId = adotadoPorId;
    }

    public boolean isAdotado() {
        return adotado;
    }

    public void setAdotado(boolean adotado) {
        this.adotado = adotado;
    }

    public String getDonoId() {
        return donoId;
    }

    public void setDonoId(String donoId) {
        this.donoId = donoId;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}