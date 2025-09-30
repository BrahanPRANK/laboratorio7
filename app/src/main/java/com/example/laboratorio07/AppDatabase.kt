package com.example.laboratorio07

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserAccess::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userAccessDao(): UserAccessDao
}
