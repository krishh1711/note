package com.task.notes.ui.noteslist

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.notes.data.repository.NotesRepository
import com.task.notes.ui.NotesListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val repository: NotesRepository,
    application: Application
) : ViewModel() {
    val appContext = application.applicationContext
    var listener: NotesListener? = null
    var listIsEmpty = ObservableField(true)
    var listCategoriesIsEmpty = ObservableField(false)
    var categoryList = ObservableField<List<String>>()

    init {
        viewModelScope.launch { }
    }

    fun getNotesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val localNoteData = repository.getNotes()
            withContext(Dispatchers.Main) {
                localNoteData?.let {
                    if (it.isEmpty()) {
                        listIsEmpty.set(true)
                    } else {
                        listIsEmpty.set(false)
                    }
                    val categories = it.map { note -> note.category }.distinct()
                    if (categories.isEmpty()) {
                        listCategoriesIsEmpty.set(true)
                    } else {
                        listCategoriesIsEmpty.set(false)
                    }
                    listener?.updateNotesLists(localNoteData)
                }
            }
        }
    }

    fun deleteNote(id: Int,position:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(id)
            withContext(Dispatchers.Main) {
                getNotesList()
                listener?.removeNoteFromList(position)
                Toast.makeText(appContext, "Notes Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }


}