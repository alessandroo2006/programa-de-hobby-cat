package com.example.jobicat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobicat.adapter.HobbyAdapter;
import com.example.jobicat.database.DatabaseHelper;
import com.example.jobicat.model.Hobby;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HobbyAdapter.OnHobbyActionListener {

    private RecyclerView rvHobbies;
    private TextView tvEmptyState;
    private FloatingActionButton fabAddHobby;
    private HobbyAdapter hobbyAdapter;
    private DatabaseHelper databaseHelper;
    private List<Hobby> hobbyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Inicializar vistas
        initViews();

        // Configurar RecyclerView
        setupRecyclerView();

        // Configurar listeners
        setupListeners();

        // Cargar hobbies
        loadHobbies();
    }

    private void initViews() {
        rvHobbies = findViewById(R.id.rvHobbies);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        fabAddHobby = findViewById(R.id.fabAddHobby);
    }

    private void setupRecyclerView() {
        hobbyAdapter = new HobbyAdapter(hobbyList, this, this);
        rvHobbies.setLayoutManager(new LinearLayoutManager(this));
        rvHobbies.setAdapter(hobbyAdapter);
    }

    private void setupListeners() {
        fabAddHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddHobbyActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void loadHobbies() {
        hobbyList = databaseHelper.getAllHobbies();
        hobbyAdapter.updateHobbyList(hobbyList);
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (hobbyList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvHobbies.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvHobbies.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteHobby(int hobbyId) {
        showDeleteConfirmationDialog(hobbyId);
    }

    private void showDeleteConfirmationDialog(int hobbyId) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Hobby")
                .setMessage("¿Estás seguro de que quieres eliminar este hobby?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    databaseHelper.deleteHobby(hobbyId);
                    loadHobbies();
                    Toast.makeText(this, "Hobby eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recargar la lista cuando se agrega o edita un hobby
            loadHobbies();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar hobbies cada vez que se regresa a la actividad
        loadHobbies();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}