package com.example.sqliteroomapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.sqliteroomapp.R
import com.example.sqliteroomapp.dao.UserDao
import com.example.sqliteroomapp.databinding.ActivityMainBinding
import com.example.sqliteroomapp.db.AppDatabase
import com.example.sqliteroomapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase
    private lateinit var dao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db-user"
        ).fallbackToDestructiveMigration()
            .build()
        dao = db.userDao()


        binding.cadastrarBttn.setOnClickListener {
            registerUser()
        }


        binding.logarBttn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val name = binding.TextNome.text.toString()
        val age = binding.TextIdade.text.toString().toIntOrNull() ?: 0
        val phone = binding.TextTelefone.text.toString()
        val course = binding.TextCurso.text.toString()
        val email = binding.TextEmail.text.toString()
        val password = binding.NumberSenha.text.toString()

        if (name.isBlank() || age == 0 || phone.isBlank() || course.isBlank() || email.isBlank() || password.isBlank()) {
            showToast("Por favor, preencha todos os campos.")
            return
        }

        val user = User(
            fullName = name,
            age = age,
            phone = phone,
            course = course,
            email = email,
            password = password
        )

        lifecycleScope.launch {

            val existingUser = withContext(Dispatchers.IO) {
                dao.getUserByEmail(email)
            }

            if (existingUser == null) {

                withContext(Dispatchers.IO) {
                    dao.insertUser(user)
                }
                showToast("Usuário cadastrado com sucesso!")

                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {

                showToast("Este email já está cadastrado.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}