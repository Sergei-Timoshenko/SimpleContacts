package dev.sergeitimoshenko.simplecontacts.ui.favorites.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.sergeitimoshenko.simplecontacts.databinding.ItemFavoriteContactBinding
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import dev.sergeitimoshenko.simplecontacts.ui.favorites.listeners.OnContactClickListener

class FavoritesAdapter(
    private val onContactClickListener: OnContactClickListener
) : ListAdapter<SimpleContact, FavoritesAdapter.ContactViewHolder>(DIFF_CALLBACK){
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

    inner class ContactViewHolder(private val binding: ItemFavoriteContactBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onContactClickListener.onContactClick(getItem(adapterPosition).phoneNumber)
            }
        }

        fun bind(contact: SimpleContact) {
            binding.apply {
                tvFavoriteContactName.text = contact.name
                if (contact.photo != null) {
                    sivFavoriteContactPhoto.setImageBitmap(contact.photo)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemFavoriteContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = getItem(position)
        holder.bind(currentContact)
    }
}