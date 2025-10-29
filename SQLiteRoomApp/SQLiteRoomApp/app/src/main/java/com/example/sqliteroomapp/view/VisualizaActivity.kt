package com.example.sqliteroomapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.sqliteroomapp.dao.UserDao
import com.example.sqliteroomapp.databinding.VisualizaBinding // Importa o binding
import com.example.sqliteroomapp.db.AppDatabase
import com.example.sqliteroomapp.model.User
import com.example.sqliteroomapp.view.adapter.UserAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VisualizaActivity : AppCompatActivity() {

    private lateinit var binding: VisualizaBinding
    private lateinit var db: AppDatabase
    private lateinit var dao: UserDao
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VisualizaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db-user"
        ).fallbackToDestructiveMigration()
            .build()
        dao = db.userDao()


        binding.VoltarMenu.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        loadUsers()
    }

    private fun setupRecyclerView() {
        // CORREÇÃO:
        // Agora usa o ID correto do seu XML, acessado via binding.
        binding.InformacoesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val userList = withContext(Dispatchers.IO) {
                dao.getAll()
            }
            userAdapter = UserAdapter(userList)

            // CORREÇÃO:
            // Define o adapter no RecyclerView correto.
            binding.InformacoesRecyclerView.adapter = userAdapter
        }
    }
}