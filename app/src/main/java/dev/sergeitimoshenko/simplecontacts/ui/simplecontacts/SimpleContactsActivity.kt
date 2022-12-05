package dev.sergeitimoshenko.simplecontacts.ui.simplecontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import dev.sergeitimoshenko.simplecontacts.R

@AndroidEntryPoint
class SimpleContactsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_contacts)
    }
}