package dev.sergeitimoshenko.simplecontacts.ui.search.listeners

import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact

interface SearchListener {
    fun onContactClick(contact: SimpleContact)

    fun onCallButtonClick(phoneNumber: String)
}