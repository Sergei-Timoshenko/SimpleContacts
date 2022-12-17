package dev.sergeitimoshenko.simplecontacts.ui.contacts

import android.os.Bundle
import android.view.DragEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.sergeitimoshenko.simplecontacts.R
import dev.sergeitimoshenko.simplecontacts.databinding.FragmentContactsBinding
import dev.sergeitimoshenko.simplecontacts.models.mappers.ContactMapper
import dev.sergeitimoshenko.simplecontacts.ui.contacts.adapters.ContactsAdapter
import dev.sergeitimoshenko.simplecontacts.ui.contacts.listeners.DeleteAreaListener
import dev.sergeitimoshenko.simplecontacts.ui.contacts.listeners.DragAndDropListener
import dev.sergeitimoshenko.simplecontacts.ui.contacts.viewmodels.ContactsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts), DeleteAreaListener, DragAndDropListener {
    private var binding: FragmentContactsBinding? = null
    private lateinit var contactsAdapter: ContactsAdapter
    private val viewModel: ContactsViewModel by viewModels()

    @Inject
    lateinit var mapper: ContactMapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactsBinding.bind(view)

        // Open keypad
        binding?.fabOpenKeypad?.setOnClickListener {
            val action = ContactsFragmentDirections.actionContactsFragmentToKeypadFragment()
            findNavController().navigate(action)
        }

        binding?.actionBar?.root?.setOnClickListener {
            val action = ContactsFragmentDirections.actionContactsFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        // Setup recycler view
        contactsAdapter = ContactsAdapter(this, this)
        setupRecyclerView()
        setupItemTouchHelper()

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactsAdapter.submitList(mapper.toSimpleContactList(contacts))
        }
    }

    private fun setupRecyclerView() {
        binding?.rvContacts?.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun setupItemTouchHelper() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentSimpleContact = contactsAdapter.currentList[position]
                    val currentContact = viewModel.getContacts()
                        .filter { contact -> contact.id == currentSimpleContact.id }[0]
                    viewModel.deleteContact(currentContact)
                    Snackbar.make(binding?.root!!, "Contact deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            viewModel.insertContact(currentContact)
                        }
                        .show()
                }
            }
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding?.rvContacts)

        binding?.root?.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    binding
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    override fun showDeleteArea() {
        binding?.fabDeleteContact?.visibility = View.VISIBLE
    }

    fun hideDeleteArea() {
        binding?.fabDeleteContact?.visibility = View.GONE
    }

    override fun onDrop(contactId: String) {
        binding?.fabDeleteContact?.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val currentContact = viewModel.getContacts()
                        .filter { contact -> contact.id == contactId}[0]
                    viewModel.deleteContact(currentContact)
                    Snackbar.make(binding?.root!!, "Contact deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            viewModel.insertContact(currentContact)
                        }
                        .show()
                    hideDeleteArea()

                    true
                }
                else -> true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}