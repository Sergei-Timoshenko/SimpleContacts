package dev.sergeitimoshenko.simplecontacts.ui.favorites

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dev.sergeitimoshenko.simplecontacts.R
import dev.sergeitimoshenko.simplecontacts.databinding.FragmentFavoritesBinding
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import dev.sergeitimoshenko.simplecontacts.ui.favorites.adapters.FavoritesAdapter
import dev.sergeitimoshenko.simplecontacts.ui.favorites.listeners.OnContactClickListener
import dev.sergeitimoshenko.simplecontacts.utils.REQUEST_CALL

class FavoritesFragment : Fragment(R.layout.fragment_favorites), OnContactClickListener {
    private var binding: FragmentFavoritesBinding? = null
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)

        // Open keypad
        binding?.fabOpenKeypad?.setOnClickListener {
            val action = FavoritesFragmentDirections.actionFavoritesFragmentToKeypadFragment()
            findNavController().navigate(action)
        }

        // setup recycler view
        favoritesAdapter = FavoritesAdapter(this)
        binding?.rvFavorites?.apply {
            adapter = favoritesAdapter
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
        }

        favoritesAdapter.submitList(listOf(SimpleContact("666", "666", "666", null, id = "1"), SimpleContact("666", "666", "666", null, id = "2"), SimpleContact("666", "666", "666", null, id = "3")))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onContactClick(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL
            )
        } else {
            val callIntent = Intent(
                Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber")
            )
            startActivity(callIntent)
        }
    }
}