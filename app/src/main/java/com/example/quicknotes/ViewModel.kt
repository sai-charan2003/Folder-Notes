package com.example.quicknotes

import android.app.Application
import android.content.Context
import android.provider.ContactsContract
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknotes.database.Note
import com.example.quicknotes.database.NoteDatabase
import com.example.quicknotes.database.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ViewModel (context: Context) : ViewModel(){

    private val repository : NoteRepository
    val allnotes: LiveData<List<Note>>
    lateinit var getnotebyid:LiveData<Note>
    var textFieldValue by mutableStateOf(TextFieldValue())

    init {
        val dao= NoteDatabase.getDatabase(context).Notedao()
        repository= NoteRepository(dao)
        allnotes=repository.allnotes
    }
    fun insert(note:Note)=viewModelScope.launch(Dispatchers.IO) {

        repository.insert(note)
    }
    fun update(note:Note)=viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }
    fun getNoteById(id: Int): LiveData<Note> {
        return repository.getNoteById(id)
    }
    fun delete(note: Note)=viewModelScope.launch(Dispatchers.IO) {

        repository.delete(note)
    }





}