package dev.sergeitimoshenko.simplecontacts.ui.recents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.sergeitimoshenko.simplecontacts.R
import dev.sergeitimoshenko.simplecontacts.databinding.FragmentRecentsBinding
import dev.sergeitimoshenko.simplecontacts.ui.favorites.FavoritesFragmentDirections

class RecentsFragment : Fragment(R.layout.fragment_recents) {
    private var binding: FragmentRecentsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecentsBinding.bind(view)

        // Open keypad
        binding?.fabOpenKeypad?.setOnClickListener {
            val action = RecentsFragmentDirections.actionRecentsFragmentToKeypadFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}