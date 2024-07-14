package com.task.notes.ui.noteslist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.task.notes.R
import com.task.notes.data.local.NotesEntity
import com.task.notes.data.model.NotesDataModel
import com.task.notes.databinding.AdapterCategoriesItemBinding
import com.task.notes.ui.NotesListener

class NotesCategoriesAdapter(
    private var context: Context,
    private var noteData: List<NotesEntity>,
    var listener: NotesListener
) :
    RecyclerView.Adapter<NotesCategoriesAdapter.NotesCategories>() {
    private lateinit var binding: AdapterCategoriesItemBinding
    private lateinit var mRecyclerView: RecyclerView
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
        mRecyclerView.itemAnimator?.changeDuration = 0
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesCategories {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.adapter_categories_item, parent, false
        )

        return NotesCategories(binding)
    }


    override fun getItemCount(): Int {
        return noteData.size
    }

    override fun onBindViewHolder(holder: NotesCategories, position: Int) {
        holder.apply {
            noteData[position].apply {
                setBinding(NotesDataModel(this))
            }

        }
    }

    inner class NotesCategories(private var binding: AdapterCategoriesItemBinding) :
        RecyclerView.ViewHolder(binding.root), NoteCategoryItemListener {

        fun setBinding(model: NotesDataModel) {
            binding.model = model
            binding.listener = this
        }

        override fun onItemSelected() {
            Log.d("", "onNotesClicked")
            val distinctCategories = noteData.map { it.category }.distinct()
            Toast.makeText(
                context,
                "Selected Item: ${distinctCategories[adapterPosition]}",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    interface NoteCategoryItemListener {
        fun onItemSelected()
    }
}