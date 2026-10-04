package com.example.parliament.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Member::class, Party::class],
    version = 2,
    exportSchema = false
)
abstract class ParliamentDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun partyDao(): PartyDao

    suspend fun refreshData(
        members: List<Member>,
        parties: List<Party>
    ) {
        withTransaction {
            memberDao().deleteAllMembers()
            memberDao().insertMembers(members)
            partyDao().insertParties(parties)
        }
    }

    companion object {

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(
                db: SupportSQLiteDatabase
            ) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS parties (
                        code TEXT NOT NULL,
                        favorite INTEGER NOT NULL DEFAULT 0,
                        PRIMARY KEY(code)
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT OR IGNORE INTO parties(code)
                    SELECT DISTINCT party FROM members
                    """.trimIndent()
                )
            }
        }

        @Volatile
        private var INSTANCE: ParliamentDatabase? = null

        fun getDatabase(context: Context): ParliamentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParliamentDatabase::class.java,
                    "parliament_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}