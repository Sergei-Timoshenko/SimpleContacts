package dev.sergeitimoshenko.simplecontacts.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sergeitimoshenko.simplecontacts.databinding.ActivityMainBinding
import dev.sergeitimoshenko.simplecontacts.models.mappers.ContactMapper
import dev.sergeitimoshenko.simplecontacts.ui.main.listeners.ActionBarListener
import dev.sergeitimoshenko.simplecontacts.ui.main.viewmodels.MainActivityViewModel
import dev.sergeitimoshenko.simplecontacts.utils.REQUEST_CALL
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActionBarListener {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var mapper: ContactMapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar

        // Hide app bar
        actionBar?.hide()

        // Show back button
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fcvMain.id) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bnvMain.setupWithNavController(navController)

        // Call permission
        if (ContextCompat.checkSelfPermission(
                binding.root.context, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL
            )
        }
    }

    override fun showActionBar() {
        supportActionBar?.show()
    }

    override fun hideActionBar() {
        supportActionBar?.hide()
    }
}