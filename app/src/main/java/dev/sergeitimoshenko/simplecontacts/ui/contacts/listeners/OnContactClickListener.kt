package dev.sergeitimoshenko.simplecontacts.ui.contacts.listeners

import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact

interface OnContactClickListener {
    fun onContactClick(contactId: String)
}