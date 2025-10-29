package com.example.sqliteroomapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sqliteroomapp.dao.UserDao
import com.example.sqliteroomapp.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao():UserDao
}
