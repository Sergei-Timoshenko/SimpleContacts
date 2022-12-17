package dev.sergeitimoshenko.simplecontacts.ui.search

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.sergeitimoshenko.simplecontacts.R
import dev.sergeitimoshenko.simplecontacts.databinding.FragmentSearchBinding
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import dev.sergeitimoshenko.simplecontacts.models.mappers.ContactMapper
import dev.sergeitimoshenko.simplecontacts.ui.keypad.KeypadFragmentDirections
import dev.sergeitimoshenko.simplecontacts.ui.main.listeners.ActionBarListener
import dev.sergeitimoshenko.simplecontacts.ui.search.adapters.SearchAdapter
import dev.sergeitimoshenko.simplecontacts.ui.search.listeners.SearchListener
import dev.sergeitimoshenko.simplecontacts.ui.search.viewmodels.SearchViewModel
import dev.sergeitimoshenko.simplecontacts.utils.REQUEST_CALL
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), SearchListener {
    private var binding: FragmentSearchBinding? = null
    private lateinit var actionBarListener: ActionBarListener
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var mapper: ContactMapper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionBarListener = context as ActionBarListener

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        actionBarListener.showActionBar()

        // Setup recycler view
        val searchAdapter = SearchAdapter(this)
        binding?.rvSearch?.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            searchAdapter.submitList(mapper.toSimpleContactList(contacts))
        }

        // Setup menu
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val searchView = menuItem.actionView as SearchView

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = true

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.setSearchQuery(newText!!)
                        return true
                    }
                })

                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onContactClick(contact: SimpleContact) {
        val action = SearchFragmentDirections.actionSearchFragmentToContactFragment()
        findNavController().navigate(action)
    }

    override fun onCallButtonClick(phoneNumber: String) {
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

    override fun onDestroy() {
        super.onDestroy()
        actionBarListener.hideActionBar()
        binding = null
    }
}