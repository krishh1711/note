package com.task.notes.ui.createnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.task.notes.R
import com.task.notes.databinding.FragmentCreateNoteBinding
import com.task.notes.ui.NotesActivity
import com.task.notes.ui.noteslist.NotesListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNoteFragment : Fragment(), CreateNoteListener {
    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var viewModel: CreateNoteViewModel

    companion object {
        @JvmStatic
        fun getInstance(): CreateNoteFragment {
            return CreateNoteFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CreateNoteViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.listener = this
        binding.googleSignInButton.setOnClickListener{
            (requireActivity() as NotesActivity).signIn()
        }
        binding.googleSignOut.setOnClickListener{
            (requireActivity() as NotesActivity).signOut()
        }

    }

    override fun moveToNotesList() {
        (requireActivity() as NotesActivity).loadFragment(NotesListFragment.getInstance())
    }

}