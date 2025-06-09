package com.example.petadoption;

public class Pet {
    public String nome;
    public int idade;
    public String raca;
    public String foto;
    public String donoId;
    public boolean adotado;
    public String adotadoPorId;
    public String imagemUrl;

    public Pet() {} // Obrigatório para Firebase

    public Pet(String nome, int idade, String raca, String foto, String donoId, boolean adotado, String adotadoPorId) {
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.foto = foto;
        this.donoId = donoId;
        this.adotado = adotado;
        this.adotadoPorId = adotadoPorId;
    }
}