package dev.sergeitimoshenko.simplecontacts.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sergeitimoshenko.simplecontacts.R
import dev.sergeitimoshenko.simplecontacts.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvMain.id) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bnvMain.setupWithNavController(navController)
    }
}