package com.example.parliament.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parties")
data class Party(
    @PrimaryKey
    val code: String,
    val favorite: Boolean = false
)