package com.example.parliament.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemberDao {

    @Query("DELETE FROM members")
    suspend fun deleteAllMembers()

    @Insert
    suspend fun insertMembers(members: List<Member>)

    @Query("SELECT * FROM members")
    suspend fun getAllMembers(): List<Member>
}