package com.task.notes.ui.noteview

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.notes.data.local.NotesEntity
import com.task.notes.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NoteViewViewModel @Inject constructor(
    private val repository: NotesRepository,
    application: Application
) : ViewModel() {
    private var appContext: Context = application.applicationContext

//    var listener: NoteViewListener? = null


    var notesTitle = ObservableField<String>()
    var notesDescription = ObservableField<String>()
    var notesContent = ObservableField<String>()
    var category = ObservableField<String>()
    var id = ObservableField<Int>()

    fun updateNoteData(noteEntity: NotesEntity) {
        this.notesTitle.set(noteEntity.title)
        this.notesDescription.set(noteEntity.description)
        this.category.set(noteEntity.category)
        this.notesContent.set(noteEntity.content)
        this.id.set(noteEntity.id)
    }
    fun getContentForShare(): String {
        return "${notesTitle.get()}\n${notesDescription.get()}\n${notesContent.get()}"
    }

    fun createNote() {
        if (notesTitle.get().isNullOrEmpty() || notesContent.get().isNullOrEmpty()) {
            Toast.makeText(appContext, "Please fill in the title and content", Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                repository.createNote(
                    id = id.get(),
                    title = notesTitle.get()!!,
                    description = notesDescription.get(),
                    content = notesContent.get()!!,
                    category = category.get()!!
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(appContext, "Note created successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


}