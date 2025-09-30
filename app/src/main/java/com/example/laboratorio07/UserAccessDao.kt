package com.example.laboratorio07

import androidx.room.*

@Dao
interface UserAccessDao {
    @Query("SELECT * FROM user_access")
    suspend fun getAll(): List<UserAccess>

    @Insert
    suspend fun insert(user: UserAccess)

    @Delete
    suspend fun delete(user: UserAccess)

    @Query("SELECT * FROM user_access ORDER BY id DESC LIMIT 1")
    suspend fun getLastUser(): UserAccess?
}
