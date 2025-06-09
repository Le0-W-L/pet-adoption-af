package com.example.petadoption;

public class Usuarios {

    private String nome;
    private String email;

    // Construtor vazio é obrigatório para o Firestore
    public Usuarios() {}

    public Usuarios(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}