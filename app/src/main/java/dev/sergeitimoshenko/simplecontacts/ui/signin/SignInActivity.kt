package dev.sergeitimoshenko.simplecontacts.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import dev.sergeitimoshenko.simplecontacts.databinding.ActivitySignInBinding
import dev.sergeitimoshenko.simplecontacts.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "SignInActivity"

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var signInIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide action bar
        supportActionBar?.hide()

        // Google sign in
        val startGoogleActivityForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
                val signInAccount = GoogleSignIn.getSignedInAccountFromIntent(intent.data).result
                signInAccount?.let { account ->
                    googleAuth(account)
                }
            }

        binding.btnSignInWithGoogle.setOnClickListener {
            startGoogleActivityForResult.launch(signInIntent)
        }
    }

    private fun googleAuth(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                navigateToSimpleContactsActivity()
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "googleAuth: ${e.message}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        currentUser?.let {
            navigateToSimpleContactsActivity()
        }
    }

    private fun navigateToSimpleContactsActivity() {
        val mainActivityIntent = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }
}