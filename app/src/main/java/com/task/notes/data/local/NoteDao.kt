package com.task.notes.data.local

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertData(notesEntity: NotesEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateData(notesEntity: NotesEntity)

    suspend fun insertOrUpdateData(notesEntity: NotesEntity) {
        if (notesEntity.id != null) {
            updateData(notesEntity)
        } else {
            insertData(notesEntity)
        }
    }
    @Query("SELECT * FROM NotesEntity")
    suspend fun getAllNotes(): List<NotesEntity>?

    @Query("SELECT * FROM NotesEntity WHERE title = :title")
    suspend fun getNoteById(title: String): NotesEntity?

    @Query("SELECT * FROM NotesEntity WHERE title = :titleName")
    suspend fun getByName(titleName: String): NotesEntity?



    @Query("DELETE FROM NotesEntity WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

}
