package dev.sergeitimoshenko.simplecontacts.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.sergeitimoshenko.simplecontacts.R
import dev.sergeitimoshenko.simplecontacts.databinding.ActivitySignInBinding
import dev.sergeitimoshenko.simplecontacts.ui.simplecontacts.SimpleContactsActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide action bar
        supportActionBar?.hide()

        // Open contacts activity
        binding.btnSignInWithGoogle.setOnClickListener {
            val simpleContactsActivityIntent = Intent(this@SignInActivity, SimpleContactsActivity::class.java)
            startActivity(simpleContactsActivityIntent)
            finish()
        }
    }
}