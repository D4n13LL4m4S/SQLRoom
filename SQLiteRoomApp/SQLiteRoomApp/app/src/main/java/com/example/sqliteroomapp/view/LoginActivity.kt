package com.example.sqliteroomapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.sqliteroomapp.dao.UserDao
import com.example.sqliteroomapp.databinding.LoginBinding
import com.example.sqliteroomapp.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var db: AppDatabase
    private lateinit var dao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db-user"
        ).fallbackToDestructiveMigration()
            .build()
        dao = db.userDao()


        binding.bttnLogar.setOnClickListener {
            login()
        }


        binding.VoltarCadastro.setOnClickListener {
            finish()
        }
    }

    private fun login() {
        val email = binding.TextLogin.text.toString()
        val password = binding.TextSenha.text.toString()

        if (email.isBlank() || password.isBlank()) {
            showToast("Por favor, preencha login e senha.")
            return
        }

        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                dao.login(email, password)
            }

            if (user != null) {
                showToast("Login bem-sucedido! Bem-vindo, ${user.fullName}.")

                val intent = Intent(this@LoginActivity, ProfileActivity::class.java)

                intent.putExtra("USER_EMAIL", user.email)
                startActivity(intent)
                finishAffinity()
            } else {
                showToast("Email ou senha inválidos.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}