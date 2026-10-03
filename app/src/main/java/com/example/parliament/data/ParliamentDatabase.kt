package com.example.parliament.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Member::class, Party::class],
    version = 2,
    exportSchema = false
)
abstract class ParliamentDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun partyDao(): PartyDao

    companion object {
        @Volatile
        private var INSTANCE: ParliamentDatabase? = null

        fun getDatabase(context: Context): ParliamentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParliamentDatabase::class.java,
                    "parliament_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}