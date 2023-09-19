package com.example.quicknotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Note::class), version = 1)

abstract class NoteDatabase :RoomDatabase() {
    abstract fun Notedao(): NoteDAO

    companion object {

        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "Notes"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}