package com.task.notes.ui.createnote

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.notes.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val repository: NotesRepository,
    application: Application
) : ViewModel() {
    private var appContext: Context = application.applicationContext

    var listener: CreateNoteListener? = null

    init {
        getNotesList()
    }

    var notesTitle = ObservableField<String>()
    var notesDescription = ObservableField<String>()
    var notesContent = ObservableField<String>()
    var category = ObservableField<String>()
    var categoryList = ObservableField<List<String?>>()
    var categorySelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            category.set(categoryList.get()?.get(position))
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }

    private fun getNotesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val localNoteData = repository.getNotes()
            withContext(Dispatchers.Main) {
                localNoteData?.let {
                    val categories = it.map { note -> note.category }.distinct()
                    categoryList.set(categories)
                }
            }
        }

    }

    fun createNote(view: View) {
        if (notesTitle.get().isNullOrEmpty() || notesContent.get().isNullOrEmpty()) {
            Toast.makeText(appContext, "Please fill in the title and content", Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                repository.createNote(
                    id = null,
                    title = notesTitle.get()!!,
                    description = notesDescription.get(),
                    content = notesContent.get()!!,
                    category = category.get()!!
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(appContext, "Note created successfully", Toast.LENGTH_SHORT)
                        .show()
                    clearFields()
                    listener?.moveToNotesList()
                }
            }
        }
    }

    private fun clearFields() {
        notesTitle.set("")
        notesDescription.set("")
        notesContent.set("")
        category.set("")
    }
}