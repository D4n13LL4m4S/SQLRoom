package com.example.sqliteroomapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sqliteroomapp.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun getAll(): List<User>

    @Insert
    suspend fun insertUser(vararg user: User)

    // --- Novos métodos necessários ---

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    /**
     * Query para o Login: Busca um usuário pelo email e senha
     */
    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    fun login(email: String, password: String): User?

    /**
     * Query para buscar um usuário pelo email (útil para a tela de perfil)
     */
    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): User?
}