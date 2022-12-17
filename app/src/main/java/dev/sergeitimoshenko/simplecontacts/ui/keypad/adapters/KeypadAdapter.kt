package dev.sergeitimoshenko.simplecontacts.ui.keypad.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.sergeitimoshenko.simplecontacts.databinding.ItemContactBinding
import dev.sergeitimoshenko.simplecontacts.databinding.ItemPhoneNumberActionsBinding
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import dev.sergeitimoshenko.simplecontacts.ui.keypad.listeners.OnActionsClickListener
import dev.sergeitimoshenko.simplecontacts.ui.keypad.listeners.OnContactClickListener
import dev.sergeitimoshenko.simplecontacts.utils.TYPE_ACTIONS
import dev.sergeitimoshenko.simplecontacts.utils.TYPE_CONTACT

class KeypadAdapter(
    private val onContactClickListener: OnContactClickListener,
    private val onActionsClickListener: OnActionsClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is SimpleContact && newItem is SimpleContact) {
                    return oldItem.id == newItem.id
                }
                if (oldItem is String && newItem is String) {
                    return true
                }
                return false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is SimpleContact && newItem is SimpleContact) {
                    return oldItem == newItem
                }
                if (oldItem is String && newItem is String) {
                    return true
                }
                return false
            }
        }
    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onContactClickListener.onContactClick(differ.currentList[adapterPosition] as SimpleContact)
                    }
                }

                ibtnCall.setOnClickListener {
                    val currentContact = differ.currentList[adapterPosition] as SimpleContact
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onContactClickListener.onCallButtonClick(currentContact.phoneNumber)
                    }
                }
            }
        }

        fun bind(currentContact: SimpleContact) {
            binding.apply {
                tvContactName.text = currentContact.name
                tvContactSurname.text = currentContact.surname
                tvContactPhoneNumber.text = currentContact.phoneNumber
                if (currentContact.photo != null) {
                    sivContactPhoto.setImageBitmap(currentContact.photo)
                }
            }
        }
    }

    inner class KeypadActionsViewHolder(private val binding: ItemPhoneNumberActionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ibAddContact.setOnClickListener {
                onActionsClickListener.onAddContactClick()
            }

            binding.ibSendSms.setOnClickListener {
                onActionsClickListener.onSmsContactClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_CONTACT) {
            val binding =
                ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ContactViewHolder(binding)
        } else {
            val binding =
                ItemPhoneNumberActionsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            KeypadActionsViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position] is SimpleContact) {
            TYPE_CONTACT
        } else {
            TYPE_ACTIONS
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_CONTACT) {
            val currentContact = differ.currentList[position]
            val contactHolder = holder as ContactViewHolder
            contactHolder.bind(currentContact as SimpleContact)
        }
    }
}