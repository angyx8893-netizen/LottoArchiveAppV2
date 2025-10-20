package com.example.lottoarchive.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "draws")
data class Draw(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val wheel: String,
    val n1: Int,
    val n2: Int,
    val n3: Int,
    val n4: Int,
    val n5: Int
)
