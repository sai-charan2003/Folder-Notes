package com.example.quicknotes.database

import androidx.lifecycle.LiveData


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
    fun getNotesInFolder(folderId: Int): LiveData<List<Note>> {
        return NoteDAO.getNotesInFolder(folderId)
    }






}
class folderRepository(private val folderDAO: FolderDAO) {

    val foldernames: LiveData<List<foldername>> = folderDAO.getfolderdata()
    lateinit var foldernamesbyid:LiveData<foldername>

    suspend fun insert(folder: foldername){
        folderDAO.insert(folder)
    }
    suspend fun update(folder: foldername){
        folderDAO.update(folder)
    }

    fun getfolderdatabyid(id: Int): LiveData<foldername> {
        return folderDAO.getfolderdatabyid(id)
    }
    suspend fun delete(folder:foldername){
        folderDAO.delete(folder)
    }







}