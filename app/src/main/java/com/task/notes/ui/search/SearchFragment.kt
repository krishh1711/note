package com.task.notes.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.notes.R
import com.task.notes.data.local.NotesEntity
import com.task.notes.databinding.FragmentSearchNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchNoteBinding
    private lateinit var viewModel: SearchViewModel

    private lateinit var notesAdapter: NoteSearchAdapter
    private var notesList: List<NotesEntity>? = null

    companion object {
        @JvmStatic
        fun getInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.viewModel = viewModel
        notesList = viewModel.itemList.get()

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("onViewCreated", "beforeTextChanged: $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    viewModel.clearSearchResults()
                } else {
                    Log.d("onViewCreated", "onTextChanged: $s")
                    viewModel.onTextChanged(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("onViewCreated", "afterTextChanged: $s")
//                filterList(s.toString())

            }
        })


        viewModel.items.observe(viewLifecycleOwner) { newItems ->
            val filteredItems = newItems.map { it.split("|")[0] }
            val adapter = ArrayAdapter(requireContext(), R.layout.item_notes, filteredItems)
            binding.searchResultsList.adapter = adapter
        }
        viewModel.selectedItem.observe(viewLifecycleOwner, Observer { selectedItem ->
            Log.d("", "Selected: ${selectedItem}")
            Toast.makeText(requireContext(), "Selected: ${selectedItem}", Toast.LENGTH_SHORT).show()
        })
//
//         Set item click listener
        binding.searchResultsList.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = binding.searchResultsList.getItemAtPosition(position) as String
            viewModel.selectItem(selectedItem)
        }


    }


    private fun filterList(query: String) {
        if (notesList != null) {
            val filteredList = notesList!!.filter {
                it.title.contains(query, ignoreCase = true)
            }
            notesAdapter = NoteSearchAdapter(filteredList) { notesEntity ->
                Log.d("not", "Selected NotesEntity: ${notesEntity.id}")
                Log.d("not", "Selected NotesEntity: ${notesEntity.title}")
            }

            binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.notesRecyclerView.adapter = notesAdapter
            notesAdapter.notifyDataSetChanged()
        } else {
            viewModel.showNoResultsFound.set(true)
        }
    }
}
