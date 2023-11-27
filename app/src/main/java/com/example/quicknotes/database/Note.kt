package com.example.quicknotes.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "Notes", foreignKeys = [ForeignKey(entity = foldername::class, parentColumns = ["id"], childColumns = ["folderid"],onDelete = ForeignKey.CASCADE)])

data class Note(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val body:String,
    val folderid:Int


)
@Entity(tableName="foldernames")

data class foldername(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,

    )
