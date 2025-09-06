package com.example.jobicat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jobicat.model.Hobby;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "jobicat.db";
    private static final int DATABASE_VERSION = 1;
    
    // Tabla hobbies
    private static final String TABLE_HOBBIES = "hobbies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_DESCRIPTION = "description";

    // Query para crear la tabla
    private static final String CREATE_TABLE_HOBBIES = 
            "CREATE TABLE " + TABLE_HOBBIES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT NOT NULL," +
            COLUMN_DIFFICULTY + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION + " TEXT" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HOBBIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOBBIES);
        onCreate(db);
    }

    // CRUD Operations

    // Crear hobby
    public long addHobby(Hobby hobby) {
        if (!hobby.isValid()) {
            return -1; // Error de validación
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, hobby.getName().trim());
        values.put(COLUMN_DIFFICULTY, hobby.getDifficulty());
        values.put(COLUMN_DESCRIPTION, hobby.getDescription() != null ? hobby.getDescription().trim() : "");

        long result = db.insert(TABLE_HOBBIES, null, values);
        db.close();
        return result;
    }

    // Obtener hobby por ID
    public Hobby getHobby(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HOBBIES,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DIFFICULTY, COLUMN_DESCRIPTION},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        Hobby hobby = null;
        if (cursor != null && cursor.moveToFirst()) {
            hobby = new Hobby(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            );
            cursor.close();
        }
        db.close();
        return hobby;
    }

    // Obtener todos los hobbies
    public List<Hobby> getAllHobbies() {
        List<Hobby> hobbyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HOBBIES + " ORDER BY " + COLUMN_NAME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Hobby hobby = new Hobby();
                hobby.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                hobby.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                hobby.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY)));
                hobby.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                hobbyList.add(hobby);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hobbyList;
    }

    // Actualizar hobby
    public int updateHobby(Hobby hobby) {
        if (!hobby.isValid()) {
            return -1; // Error de validación
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, hobby.getName().trim());
        values.put(COLUMN_DIFFICULTY, hobby.getDifficulty());
        values.put(COLUMN_DESCRIPTION, hobby.getDescription() != null ? hobby.getDescription().trim() : "");

        int result = db.update(TABLE_HOBBIES, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(hobby.getId())});
        db.close();
        return result;
    }

    // Eliminar hobby
    public void deleteHobby(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOBBIES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Obtener conteo de hobbies
    public int getHobbiesCount() {
        String countQuery = "SELECT * FROM " + TABLE_HOBBIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}
