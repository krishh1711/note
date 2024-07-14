package com.task.notes.ui.noteslist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.task.notes.R
import com.task.notes.data.local.NotesEntity
import com.task.notes.databinding.FragmentNotesListBinding
import com.task.notes.ui.NotesListener
import com.task.notes.ui.noteview.NoteViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesListFragment : Fragment(), NotesListener {
    private lateinit var binding: FragmentNotesListBinding
    private lateinit var viewModel: NotesListViewModel
    private var adapter: NotesListAdapter? = null
    private var categoriesAdapter: NotesCategoriesAdapter? = null
    private var adapterListener: NotesListAdapter.NoteItemListener? = null


    companion object {
        @JvmStatic
        fun getInstance(): NotesListFragment {
            return NotesListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NotesListViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.listener = this

        binding.apply {
            notesList.layoutManager = LinearLayoutManager(requireContext())
            categoriesList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        viewModel.getNotesList()

    }

    override fun createNewNote() {
        Log.d("NotesListFragment", "createNewNote: Not yet implemented")
    }

    override fun viewMoteDetails(notesEntity: NotesEntity, edit: Boolean) {
        Log.d("NotesListFragment", "viewMoteDetails: Not yet implemented")
        val intent = Intent(requireContext(), NoteViewActivity::class.java)
        intent.putExtra("notesEntity", Gson().toJson(notesEntity))
        intent.putExtra("edit", edit)
        startActivity(intent)
    }

    override fun showMoreOptions(notesEntity: NotesEntity, adapterPosition: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Options")
            .setItems(R.array.note_options) { _, which ->
                when (which) {
                    0 -> {
                        viewMoteDetails(notesEntity, false)
                    }

                    1 -> {
                        viewMoteDetails(notesEntity, true)
                    }

                    2 -> {
                        viewModel.deleteNote(notesEntity.id!!, adapterPosition)
                    }

                    3 -> {
                        // Share option
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "${notesEntity.title}: ${notesEntity.content}"
                        )
                        startActivity(Intent.createChooser(shareIntent, "Share Note"))
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun removeNoteFromList(position: Int) {
        adapter?.deleteNote(position)
    }

    override fun updateNotesLists(notesData: List<NotesEntity>) {
        if (notesData.isNotEmpty()) {
            if (adapter == null) {
                adapter = NotesListAdapter(
                    requireContext(), notesData as ArrayList<NotesEntity>, this
                )
                categoriesAdapter = NotesCategoriesAdapter(
                    requireContext(), notesData, this
                )
                binding.notesList.adapter = adapter
                binding.categoriesList.adapter = categoriesAdapter
            } else {
                Log.d("NotesListFragment", "notes data empty")
            }
        }
    }
}