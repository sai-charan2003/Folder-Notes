package com.example.quicknotes.database


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao

interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)
    @Update
    suspend fun update(note: Note)
    @Delete
    suspend fun delete(note: Note)
    @Query("SELECT * FROM  Notes ORDER BY ID ASC")
    fun getdata(): LiveData<List<Note>>
//    @Query("SELECT * FROM Notes WHERE ID=id")
//    fun getdatabyid(id:Int): LiveData<Note>
    @Query("SELECT * FROM Notes WHERE ID = :id")
    fun getdatabyid(id:Int):LiveData<Note>
    @Query("SELECT * FROM notes WHERE folderId = :folderId")
    fun getNotesInFolder(folderId: Int): LiveData<List<Note>>


}
@Dao

interface FolderDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder: foldername)

    @Delete
    suspend fun delete(folder: foldername)
    @Query("SELECT * FROM  foldernames ORDER BY id ASC")
    fun getfolderdata(): LiveData<List<foldername>>

    @Query("SELECT * FROM foldernames WHERE id = :id")
    fun getfolderdatabyid(id:Int):LiveData<foldername>
    @Update
    suspend fun update(folder: foldername)


}