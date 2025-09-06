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

public class AddHobbyActivity extends AppCompatActivity {

    private TextInputEditText etHobbyName, etDescription;
    private TextInputLayout tilHobbyName, tilDescription;
    private RadioButton rbEasy, rbMedium, rbHard;
    private MaterialButton btnSave, btnCancel;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hobby);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Inicializar vistas
        initViews();

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
        
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHobby();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveHobby() {
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

        // Crear objeto hobby
        Hobby hobby = new Hobby(name, difficulty, description);

        // Guardar en base de datos
        long result = databaseHelper.addHobby(hobby);

        if (result != -1) {
            Toast.makeText(this, "Hobby guardado exitosamente", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Error al guardar el hobby", Toast.LENGTH_SHORT).show();
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
