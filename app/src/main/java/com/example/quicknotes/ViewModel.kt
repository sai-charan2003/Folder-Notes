package com.example.quicknotes

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknotes.database.Note
import com.example.quicknotes.database.NoteDAO

import com.example.quicknotes.database.NoteRepository
import com.example.quicknotes.database.NotesDatabase
import com.example.quicknotes.database.folderRepository
import com.example.quicknotes.database.foldername


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel (context: Context) : ViewModel(){
    val sharedPreferences: SharedPreferences =context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE)
    val editor=sharedPreferences.edit()



    private val repository : NoteRepository
    val allnotes: LiveData<List<Note>>
    val foldernames: LiveData<List<foldername>>
    lateinit var getnotebyid:LiveData<Note>
    var textFieldValue by mutableStateOf(TextFieldValue())
    private val folderrepository: folderRepository

    init {
        val dao= NotesDatabase.getDatabase(context).noteDao()
        val folderdao= NotesDatabase.getDatabase(context).folderDao()
        repository= NoteRepository(dao)
        folderrepository = folderRepository(folderdao)
        allnotes=repository.allnotes
        foldernames=folderrepository.foldernames
        editor.putBoolean("showsnackbar",true).apply()
    }
    fun insert(note:Note)=viewModelScope.launch(Dispatchers.IO) {

        repository.insert(note)
    }
    fun update(note:Note)=viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }
    fun update(folder: foldername)=viewModelScope.launch(Dispatchers.IO){
        folderrepository.update(folder)
    }
    fun getNoteById(id: Int): LiveData<Note> {
        return repository.getNoteById(id)
    }
    fun getfolderbyid(id:Int): LiveData<foldername>{
        return folderrepository.getfolderdatabyid(id)
    }
    fun delete(note: Note)=viewModelScope.launch(Dispatchers.IO) {

        repository.delete(note)
    }
    fun insertfolderdata(folder: foldername)=viewModelScope.launch(Dispatchers.IO) {
        folderrepository.insert(folder)
    }
    fun delete(folder:foldername)=viewModelScope.launch(Dispatchers.IO) {
        folderrepository.delete(folder)
    }
    fun getNotesInFolder(folderId: Int): LiveData<List<Note>> {
        return repository.getNotesInFolder(folderId)
    }







}