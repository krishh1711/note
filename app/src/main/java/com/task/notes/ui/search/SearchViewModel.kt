package com.task.notes.ui.search

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.notes.data.local.NotesEntity
import com.task.notes.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {
    var showNoResultsFound = ObservableField(false)

    var itemList = ObservableField<List<NotesEntity>>()
    var searchKeyList = ObservableField<List<String>>()
    var searchResultsIds = ObservableField<List<Int>>()

    private val _items = MutableLiveData<List<String>>()
    val items: LiveData<List<String>> get() = _items

    private val _selectedItem = MutableLiveData<String>()
    val selectedItem: LiveData<String> get() = _selectedItem

    fun setItems(newItems: List<String>) {
        _items.value = newItems
    }

    fun selectItem(item: String) {
        _selectedItem.value = item
    }

    fun onTextChanged(newText: String) {
        Log.d("SearchViewModel", "onTextChanged: $newText")
        viewModelScope.launch(Dispatchers.IO) {
            val searchResults =
                searchKeyList.get()?.filter { it.contains(newText, ignoreCase = true) }
                    ?: emptyList()
            val searchResultsIds =
                searchKeyList.get()?.filter { it.contains(newText, ignoreCase = true) }
                    ?.map { it.length }
                    ?: emptyList()

            withContext(Dispatchers.Main) {
                Log.d("SearchViewModel", "onTextChanged: $searchResults")
                setItems(searchResults)
                this@SearchViewModel.searchResultsIds.set(searchResultsIds)
            }
        }
    }

    fun clearSearchResults() {
        setItems(emptyList())
    }

    init {
        getNotesList()
    }

    private fun getNotesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val localNoteData = repository.getNotes()
            withContext(Dispatchers.Main) {
                localNoteData?.let {
                    val titles = it.map { note -> note.title }
                    searchKeyList.set(titles)
                    itemList.set(it)
                }
            }
        }
    }

     fun getByName(name: String) {

    }

}