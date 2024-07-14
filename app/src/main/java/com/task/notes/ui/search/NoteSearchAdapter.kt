package com.task.notes.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.task.notes.R
import com.task.notes.data.local.NotesEntity

class NoteSearchAdapter(
    private val notesList: List<NotesEntity>,
    private val onItemClick: (NotesEntity) -> Unit
) : RecyclerView.Adapter<NoteSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notesList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = notesList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)

        init {
            itemView.setOnClickListener {
                onItemClick(notesList[adapterPosition])
            }
        }

        fun bind(notesEntity: NotesEntity) {
            titleTextView.text = notesEntity.title
        }
    }
}
