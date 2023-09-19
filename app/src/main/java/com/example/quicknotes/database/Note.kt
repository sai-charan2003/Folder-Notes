package com.example.quicknotes.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")

data class Note(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val body:String


)
