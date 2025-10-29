package com.example.sqliteroomapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.sqliteroomapp.dao.UserDao
import com.example.sqliteroomapp.databinding.ActivityProfileBinding
import com.example.sqliteroomapp.db.AppDatabase
import com.example.sqliteroomapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var db: AppDatabase
    private lateinit var dao: UserDao

    private var currentUser: User? = null
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db-user"
        ).fallbackToDestructiveMigration()
            .build()
        dao = db.userDao()


        userEmail = intent.getStringExtra("USER_EMAIL")

        if (userEmail == null) {
            showToast("Erro: Email do usuário não encontrado.")
            finish()
            return
        }

        loadUserProfile(userEmail!!)


        binding.btnUpdate.setOnClickListener {
            updateUser()
        }


        binding.btnDelete.setOnClickListener {
            deleteUser()
        }


        binding.btnViewAll.setOnClickListener {
            val intent = Intent(this, VisualizaActivity::class.java)
            startActivity(intent)
        }


        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun loadUserProfile(email: String) {
        lifecycleScope.launch {
            currentUser = withContext(Dispatchers.IO) {
                dao.getUserByEmail(email)
            }

            currentUser?.let { user ->
                binding.textWelcome.text = "Bem-vindo, ${user.fullName.split(" ")[0]}"
                binding.editNome.setText(user.fullName)
                binding.editIdade.setText(user.age.toString())
                binding.editTelefone.setText(user.phone)
                binding.editCurso.setText(user.course)
                binding.editEmail.setText(user.email)
            }
        }
    }

    private fun updateUser() {
        val name = binding.editNome.text.toString()
        val age = binding.editIdade.text.toString().toIntOrNull() ?: 0
        val phone = binding.editTelefone.text.toString()
        val course = binding.editCurso.text.toString()

        if (name.isBlank() || age == 0 || phone.isBlank() || course.isBlank()) {
            showToast("Todos os campos devem ser preenchidos.")
            return
        }


        currentUser?.let {
            val updatedUser = it.copy(
                fullName = name,
                age = age,
                phone = phone,
                course = course

            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    dao.updateUser(updatedUser)
                }
                currentUser = updatedUser
                showToast("Dados atualizados com sucesso!")
            }
        }
    }

    private fun deleteUser() {
        currentUser?.let { user ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    dao.deleteUser(user)
                }
                showToast("Conta deletada com sucesso.")
                logout()
            }
        }
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}