package dev.sergeitimoshenko.simplecontacts.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.sergeitimoshenko.simplecontacts.databinding.ItemContactBinding
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import dev.sergeitimoshenko.simplecontacts.ui.search.listeners.SearchListener

class SearchAdapter(
    private val searchListener: SearchListener
) : ListAdapter<SimpleContact, SearchAdapter.ContactViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SimpleContact>() {
            override fun areItemsTheSame(oldItem: SimpleContact, newItem: SimpleContact): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SimpleContact,
                newItem: SimpleContact
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val currentContact = getItem(adapterPosition)
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        searchListener.onContactClick(currentContact)
                    }
                }

                ibtnCall.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        searchListener.onCallButtonClick(currentList[adapterPosition].phoneNumber)
                    }
                }
            }
        }

        fun bind(contact: SimpleContact) {
            binding.apply {
                tvContactName.text = contact.name
                tvContactSurname.text = contact.surname
                tvContactPhoneNumber.text = contact.phoneNumber
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding =
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = getItem(position)
        holder.bind(currentContact)
    }

}