package com.task.notes.data.repository

import com.task.notes.data.local.NoteDao
import com.task.notes.data.local.NotesEntity
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val dao: NoteDao
) {
    suspend fun getNotes(): List<NotesEntity>? {
        return dao.getAllNotes()
    }

    suspend fun createNote(
        id: Int?,
        title: String,
        description: String?,
        content: String,
        category: String?
    ) {
        dao.insertOrUpdateData(
            NotesEntity(
                id = id,
                title = title,
                description = description,
                content = content,
                category = category
            )
        )
    }

    suspend fun deleteNote(id: Int) {
        dao.deleteNoteById(id)
    }

    suspend fun getByName(name:String): NotesEntity? {
        return dao.getByName(name)
    }
}