package com.example.parliament.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Query("DELETE FROM members")
    suspend fun deleteAllMembers()

    @Insert
    suspend fun insertMembers(members: List<Member>)

    @Query("SELECT * FROM members")
    fun getAllMembers(): Flow<List<Member>>

    @Query("""
        SELECT * FROM members
        WHERE party = :party
        ORDER BY lastname, firstname
    """)
    fun getMembersByParty(party: String): Flow<List<Member>>
}