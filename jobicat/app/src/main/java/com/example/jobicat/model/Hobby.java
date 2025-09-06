package com.example.jobicat.model;

public class Hobby {
    private int id;
    private String name;
    private String difficulty;
    private String description;

    // Constructor vacío
    public Hobby() {}

    // Constructor con parámetros
    public Hobby(String name, String difficulty, String description) {
        this.name = name;
        this.difficulty = difficulty;
        this.description = description;
    }

    // Constructor completo
    public Hobby(int id, String name, String difficulty, String description) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.description = description;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Método para validar datos
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() && 
               difficulty != null && !difficulty.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Hobby{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
