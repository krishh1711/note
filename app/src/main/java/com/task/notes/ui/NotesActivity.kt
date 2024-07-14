package com.task.notes.ui


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.task.notes.R
import com.task.notes.databinding.ActivityNotesBinding
import com.task.notes.ui.createnote.CreateNoteFragment
import com.task.notes.ui.noteslist.NotesListFragment
import com.task.notes.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotesActivity : AppCompatActivity() {
    private lateinit var viewModel: NotesViewModel
    private lateinit var binding: ActivityNotesBinding
    private lateinit var auth: FirebaseAuth
    private var mSignInClient: GoogleSignInClient? = null

    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 200L
                slideUp.doOnEnd {
                    splashScreenView.remove()
                }
                slideUp.start()
            }
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes)
        ViewCompat.setOnApplyWindowInsetsListener(binding.notes) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        binding.viewModel = viewModel

        // Setting up bottom navigation listener
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.task.notes.R.id.home -> {
                    loadFragment(NotesListFragment.getInstance())
                    true
                }

                com.task.notes.R.id.search -> {
                    // Assuming there's a SearchFragment for search functionality
                    loadFragment(SearchFragment.getInstance())
                    true
                }

                com.task.notes.R.id.edit -> {
                    // Assuming there's a CreateNoteFragment for edit functionality
                    loadFragment(CreateNoteFragment.getInstance())
                    true
                }

                else -> false
            }
        }
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.home
        }
        // Load the home fragment by default
        loadFragment(NotesListFragment.getInstance())

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
    }

    fun loadFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val existingFragment =
            supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)

        if (existingFragment != null) {
            fragmentTransaction.replace(
                R.id.frameLayout,
                existingFragment,
                fragment.javaClass.simpleName
            )
        } else {
            fragmentTransaction.replace(
                R.id.frameLayout,
                fragment, fragment.javaClass.simpleName
            )

        }

        fragmentTransaction.commit()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.w("TAG", "Google sign in failed")
                signInWithCredential(account)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }


    private fun signInWithCredential(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener(this) {
                Log.w("TAG", "Google sign in success${auth.currentUser}")
                Toast.makeText(
                    this, "Google sign in success",
                    Toast.LENGTH_LONG
                ).show()

            }
            .addOnFailureListener(this) { e ->
                Toast.makeText(
                    this, "Authentication failed.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    fun signOut() {
        auth.signOut()
    }
    fun signIn() {
        val signInIntent = mSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

}