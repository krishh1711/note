package com.task.notes.ui

import android.icu.text.Transliterator.Position
import com.task.notes.data.local.NotesEntity

interface NotesListener {
    fun createNewNote()
    fun updateNotesLists(notesData: List<NotesEntity>)
    fun viewMoteDetails(notesEntity: NotesEntity, edit: Boolean)
    fun showMoreOptions(notesEntity: NotesEntity,adapterPosition: Int)
    fun removeNoteFromList(position: Int)
}