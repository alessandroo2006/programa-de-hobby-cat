package com.example.jobicat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobicat.database.DatabaseHelper;
import com.example.jobicat.model.Hobby;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditHobbyActivity extends AppCompatActivity {

    private TextInputEditText etHobbyName, etDescription;
    private TextInputLayout tilHobbyName, tilDescription;
    private RadioButton rbEasy, rbMedium, rbHard;
    private MaterialButton btnUpdate, btnCancel;
    private DatabaseHelper databaseHelper;
    private Hobby hobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hobby);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Obtener hobby a editar
        Intent intent = getIntent();
        int hobbyId = intent.getIntExtra("hobby_id", -1);
        
        if (hobbyId == -1) {
            Toast.makeText(this, "Error: Hobby no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cargar hobby desde base de datos
        hobby = databaseHelper.getHobby(hobbyId);
        if (hobby == null) {
            Toast.makeText(this, "Error: Hobby no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar vistas
        initViews();

        // Cargar datos del hobby
        loadHobbyData();

        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        etHobbyName = findViewById(R.id.etHobbyName);
        etDescription = findViewById(R.id.etDescription);
        tilHobbyName = findViewById(R.id.tilHobbyName);
        tilDescription = findViewById(R.id.tilDescription);
        
        rbEasy = findViewById(R.id.rbEasy);
        rbMedium = findViewById(R.id.rbMedium);
        rbHard = findViewById(R.id.rbHard);
        
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadHobbyData() {
        etHobbyName.setText(hobby.getName());
        etDescription.setText(hobby.getDescription());

        // Seleccionar el radio button correspondiente
        switch (hobby.getDifficulty()) {
            case "Fácil":
                rbEasy.setChecked(true);
                break;
            case "Medio":
                rbMedium.setChecked(true);
                break;
            case "Difícil":
                rbHard.setChecked(true);
                break;
            default:
                rbEasy.setChecked(true);
                break;
        }
    }

    private void setupListeners() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHobby();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateHobby() {
        // Limpiar errores previos
        clearErrors();

        // Validar datos
        if (!validateInput()) {
            return;
        }

        // Obtener datos del formulario
        String name = etHobbyName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String difficulty = getSelectedDifficulty();

        // Actualizar objeto hobby
        hobby.setName(name);
        hobby.setDescription(description);
        hobby.setDifficulty(difficulty);

        // Actualizar en base de datos
        int result = databaseHelper.updateHobby(hobby);

        if (result > 0) {
            Toast.makeText(this, "Hobby actualizado exitosamente", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar el hobby", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput() {
        boolean isValid = true;

        // Validar nombre
        String name = etHobbyName.getText().toString().trim();
        if (name.isEmpty()) {
            tilHobbyName.setError("El nombre del hobby es requerido");
            isValid = false;
        } else if (name.length() < 2) {
            tilHobbyName.setError("El nombre debe tener al menos 2 caracteres");
            isValid = false;
        }

        // Validar descripción (opcional, pero si se proporciona debe tener al menos 5 caracteres)
        String description = etDescription.getText().toString().trim();
        if (!description.isEmpty() && description.length() < 5) {
            tilDescription.setError("La descripción debe tener al menos 5 caracteres");
            isValid = false;
        }

        return isValid;
    }

    private String getSelectedDifficulty() {
        if (rbEasy.isChecked()) {
            return "Fácil";
        } else if (rbMedium.isChecked()) {
            return "Medio";
        } else if (rbHard.isChecked()) {
            return "Difícil";
        }
        return "Fácil"; // Default
    }

    private void clearErrors() {
        tilHobbyName.setError(null);
        tilDescription.setError(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
