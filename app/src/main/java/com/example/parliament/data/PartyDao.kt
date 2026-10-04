package com.example.parliament.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PartyDao {

    @Query("UPDATE parties SET favorite = :favorite WHERE code = :code")
    suspend fun setFavorite(code: String, favorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertParties(parties: List<Party>)

    @Query("""
    SELECT
        p.code AS code,
        p.favorite AS favorite,
        COUNT(m.hetekaId) AS memberCount
    FROM parties p
    LEFT JOIN members m ON p.code = m.party
    GROUP BY p.code, p.favorite
    ORDER BY p.code
""")
    fun getPartiesWithMemberCount(): Flow<List<PartyWithMemberCount>>
}