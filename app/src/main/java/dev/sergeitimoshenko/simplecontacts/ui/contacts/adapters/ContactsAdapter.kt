package dev.sergeitimoshenko.simplecontacts.ui.contacts.adapters

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.sergeitimoshenko.simplecontacts.databinding.ItemContactBinding
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import dev.sergeitimoshenko.simplecontacts.ui.contacts.listeners.DeleteAreaListener
import dev.sergeitimoshenko.simplecontacts.ui.contacts.listeners.DragAndDropListener
import dev.sergeitimoshenko.simplecontacts.ui.contacts.listeners.OnContactClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsAdapter(
    private val deleteAreaListener: DeleteAreaListener,
    private val dragAndDropListener: DragAndDropListener,
    private val onContactClickListener: OnContactClickListener
) :
    ListAdapter<SimpleContact, ContactsAdapter.ContactViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SimpleContact>() {
            override fun areItemsTheSame(oldItem: SimpleContact, newItem: SimpleContact): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SimpleContact,
                newItem: SimpleContact
            ): Boolean {
                return newItem == oldItem
            }
        }
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                CoroutineScope(Dispatchers.Main).launch {
                    root.setOnLongClickListener { view ->
                        val data = ClipData.newPlainText("", "")
                        val shadowBuilder = View.DragShadowBuilder(view)

                        view.startDragAndDrop(data, shadowBuilder, view, 0)

                        deleteAreaListener.showDeleteArea()
                        dragAndDropListener.onDeleteAreaFabDrop(getItem(adapterPosition).id)

                        true
                    }
                }
                root.setOnClickListener {
                    onContactClickListener.onContactClick(getItem(adapterPosition).id)
                }
            }
        }

        fun bind(contact: SimpleContact) {
            binding.apply {
                tvContactName.text = contact.name
                tvContactSurname.text = contact.surname
                tvContactPhoneNumber.text = contact.phoneNumber
                if (contact.photo != null) {
                    sivContactPhoto.setImageBitmap(contact.photo)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = getItem(position)
        holder.bind(currentContact)
    }
}