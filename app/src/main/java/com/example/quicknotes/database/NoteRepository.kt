package com.example.quicknotes.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow


class NoteRepository(private val NoteDAO:NoteDAO) {

    val allnotes: LiveData<List<Note>> = NoteDAO.getdata()
    lateinit var notebyid:LiveData<Note>

    suspend fun insert(note: Note){
        NoteDAO.insert(note)
    }
    suspend fun update(note: Note){
        NoteDAO.update(note)
    }
    fun getNoteById(id: Int): LiveData<Note> {
        return NoteDAO.getdatabyid(id)
    }
    suspend fun delete(note: Note){
        NoteDAO.delete(note)
    }






}