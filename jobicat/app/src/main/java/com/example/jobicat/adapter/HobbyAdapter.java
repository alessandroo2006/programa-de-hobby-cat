package com.example.jobicat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobicat.EditHobbyActivity;
import com.example.jobicat.R;
import com.example.jobicat.model.Hobby;

import java.util.List;

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.HobbyViewHolder> {

    private List<Hobby> hobbyList;
    private Context context;
    private OnHobbyActionListener listener;

    public interface OnHobbyActionListener {
        void onDeleteHobby(int hobbyId);
    }

    public HobbyAdapter(List<Hobby> hobbyList, Context context, OnHobbyActionListener listener) {
        this.hobbyList = hobbyList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hobby, parent, false);
        return new HobbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HobbyViewHolder holder, int position) {
        Hobby hobby = hobbyList.get(position);
        holder.bind(hobby);
    }

    @Override
    public int getItemCount() {
        return hobbyList.size();
    }

    public void updateHobbyList(List<Hobby> newHobbyList) {
        this.hobbyList = newHobbyList;
        notifyDataSetChanged();
    }

    class HobbyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHobbyName, tvDescription, tvDifficulty;
        private Button btnEdit, btnDelete;

        public HobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHobbyName = itemView.findViewById(R.id.tvHobbyName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Hobby hobby) {
            tvHobbyName.setText(hobby.getName());
            tvDescription.setText(hobby.getDescription() != null && !hobby.getDescription().isEmpty() 
                ? hobby.getDescription() : "Sin descripción");
            tvDifficulty.setText(hobby.getDifficulty());

            // Configurar color del badge de dificultad
            int difficultyColor = getDifficultyColor(hobby.getDifficulty());
            tvDifficulty.setBackgroundColor(difficultyColor);

            // Configurar listeners de botones
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditHobbyActivity.class);
                    intent.putExtra("hobby_id", hobby.getId());
                    context.startActivity(intent);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeleteHobby(hobby.getId());
                    }
                }
            });
        }

        private int getDifficultyColor(String difficulty) {
            switch (difficulty) {
                case "Fácil":
                    return context.getResources().getColor(R.color.difficulty_easy);
                case "Medio":
                    return context.getResources().getColor(R.color.difficulty_medium);
                case "Difícil":
                    return context.getResources().getColor(R.color.difficulty_hard);
                default:
                    return context.getResources().getColor(R.color.difficulty_easy);
            }
        }
    }
}
