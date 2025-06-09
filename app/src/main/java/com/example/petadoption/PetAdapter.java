package com.example.petadoption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.MyViewHolder> {

    private List<Pet> lista;
    private Context context;

    public PetAdapter(List<Pet> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_pet, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pet pet = lista.get(position);
        holder.nome.setText(pet.nome);

        if (pet.imagemUrl != null && !pet.imagemUrl.isEmpty()) {
            Glide.with(context).load(pet.imagemUrl).into(holder.imagem);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome, desc;
        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvPetName);
            desc = itemView.findViewById(R.id.tvPetDescription);
            imagem = itemView.findViewById(R.id.imagePet);
        }
    }
}