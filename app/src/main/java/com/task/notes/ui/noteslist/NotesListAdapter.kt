package com.task.notes.ui.noteslist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.task.notes.R
import com.task.notes.data.local.NotesEntity
import com.task.notes.data.model.NotesDataModel
import com.task.notes.databinding.AdapterNoteItemBinding
import com.task.notes.ui.NotesListener

class NotesListAdapter(
    private var context: Context,
    private var notesList: ArrayList<NotesEntity>,
    var listener: NotesListener
) :
    RecyclerView.Adapter<NotesListAdapter.NoteViewHolder>() {
    private lateinit var binding: AdapterNoteItemBinding
    private lateinit var mRecyclerView: RecyclerView
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
        mRecyclerView.itemAnimator?.changeDuration = 0
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.adapter_note_item, parent, false
        )

        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.apply {
            notesList[position].apply {
                setBinding(NotesDataModel(this))
                binding.noteItem.setOnLongClickListener{
                    listener.showMoreOptions(this,position)
                    true
                }
            }
        }
    }

    inner class NoteViewHolder(private var binding: AdapterNoteItemBinding) :
        RecyclerView.ViewHolder(binding.root), NoteItemListener {

        fun setBinding(model: NotesDataModel) {
            binding.model = model
            binding.listener = this

        }

        override fun onNotesClicked() {
            Log.d("", "onNotesClicked")
            notesList[adapterPosition].apply {
                listener.viewMoteDetails(this,false)
            }
        }

        private fun onNoteLongPressed() {

            Log.d("", "onNoteLongPressed")
        }


    }
    fun deleteNote(position: Int){
        notesList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, notesList.size)
    }

    interface NoteItemListener {
        fun onNotesClicked()
    }
}