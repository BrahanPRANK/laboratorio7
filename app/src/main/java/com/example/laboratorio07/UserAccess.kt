package com.example.laboratorio07

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_access")
data class UserAccess(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val rol: String, // Alumno o Docente
    val codigo: String // Podría ser un PIN o ID único
)
