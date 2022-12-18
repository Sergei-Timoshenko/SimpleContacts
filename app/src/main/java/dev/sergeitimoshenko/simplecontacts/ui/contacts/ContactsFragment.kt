package dev.sergeitimoshenko.simplecontacts.ui.contacts

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
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
import dev.sergeitimoshenko.simplecontacts.ui.contacts.listeners.OnContactClickListener
import dev.sergeitimoshenko.simplecontacts.ui.contacts.viewmodels.ContactsViewModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts), DeleteAreaListener, DragAndDropListener, OnContactClickListener {
    private var binding: FragmentContactsBinding? = null
    private lateinit var contactsAdapter: ContactsAdapter
    private val viewModel: ContactsViewModel by viewModels()

    @Inject
    lateinit var mapper: ContactMapper

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    if (binding?.clContact?.visibility == View.VISIBLE) {
                        binding?.apply {
                            clContact.visibility = View.GONE
                            fabOpenKeypad.visibility = View.VISIBLE
                        }
                        // Thing I don't understand, but it works
                    } else if (findNavController().backQueue.size > 2) {
                        findNavController().popBackStack()
                    } else {
                        activity?.finish()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactsBinding.bind(view)

        // Custom action bar
        setupActionBar()

        // DragAndDrop listener
        setupOnRootDrop()

        // Setup recycler view
        contactsAdapter = ContactsAdapter(this, this, this)
        setupRecyclerView()
        setupItemTouchHelper()

        // Open keypad
        openKeypad()

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactsAdapter.submitList(mapper.toSimpleContactList(contacts))
        }
    }

    private fun setupActionBar() {
        binding?.actionBar?.root?.setOnClickListener {
            val action = ContactsFragmentDirections.actionContactsFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupOnRootDrop() {
        binding?.root?.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    binding?.fabDeleteContact?.visibility = View.GONE

                    true
                }
                else -> {
                    true
                }
            }
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
    }

    private fun openKeypad() {
        binding?.fabOpenKeypad?.setOnClickListener {
            val action = ContactsFragmentDirections.actionContactsFragmentToKeypadFragment()
            findNavController().navigate(action)
        }
    }

    private fun hideDeleteArea() {
        binding?.fabDeleteContact?.visibility = View.GONE
    }

    override fun showDeleteArea() {
        binding?.fabDeleteContact?.visibility = View.VISIBLE
    }

    override fun onDeleteAreaFabDrop(contactId: String) {
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

    override fun onContactClick(contactId: String) {
        val contact = viewModel.getContacts().filter { contact -> contact.id == contactId }[0]

        binding?.apply {
            clContact.visibility = View.VISIBLE
            tvName.text = contact.name
            tvSurname.text = contact.surname
            tvPhoneNumber.text = contact.phoneNumber
            tvEmail.text = contact.email
            sivContactPhoto.setImageBitmap(contact.photo)
            ibtnEditContact.setOnClickListener {
                var photo = contact.photo
                val byteStream = ByteArrayOutputStream()
                photo?.compress(Bitmap.CompressFormat.PNG, 1, byteStream)
                val action = ContactsFragmentDirections.actionContactsFragmentToEditContactFragment(contact.copy(photo = photo))
                findNavController().navigate(action)
            }

            if (contact.email.isNullOrBlank()) {
                ibtnEmail.visibility = View.GONE
            }

            // Hide fab
            fabOpenKeypad.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}