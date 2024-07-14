package com.task.notes.ui.noteview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.task.notes.R
import com.task.notes.data.local.NotesEntity
import com.task.notes.databinding.ActivityNoteViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteViewBinding
    private lateinit var viewModel: NoteViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note_view)
        viewModel = ViewModelProvider(this)[NoteViewViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(binding.viewLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.viewModel = viewModel
        val jsonString = intent.getStringExtra("notesEntity")
        val edit = intent.getBooleanExtra("edit",false)
        val notesEntity = Gson().fromJson(jsonString, NotesEntity::class.java)
        notesEntity?.let {
            viewModel.updateNoteData(it)
            if(edit){
                enableEdittextViews()
            }
        }
        binding.editButton.setOnClickListener {
            enableEdittextViews()
        }
        binding.saveButton.setOnClickListener{
            viewModel.createNote()
            disableEdittextViews()
        }

        binding.share.setOnClickListener{
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, viewModel.getContentForShare())
                startActivity(Intent.createChooser(intent, "Share your notes"))
            }
    }

    fun enableEdittextViews() {
        binding.apply {
            titleEditText.isEnabled = true
            descriptionEditText.isEnabled = true
            categoryTextView.isEnabled = true
            contentEditText.isEnabled = true
            saveButton.visibility = View.VISIBLE
            editButton.visibility = View.GONE

        }
    }

    fun disableEdittextViews() {
        binding.apply {
            titleEditText.isEnabled = false
            descriptionEditText.isEnabled = false
            categoryTextView.isEnabled = false
            contentEditText.isEnabled = false
            saveButton.visibility = View.GONE
            editButton.visibility = View.VISIBLE
        }
    }
}