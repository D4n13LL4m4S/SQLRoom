package com.example.sqliteroomapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table") // É uma boa prática nomear a tabela
data class User(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0, // Adicionado valor padrão para facilitar a inserção

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "age")
    val age: Int,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "course")
    val course: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String
)