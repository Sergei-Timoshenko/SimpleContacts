package dev.sergeitimoshenko.simplecontacts.ui.keypad.listeners

import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact

interface OnContactClickListener {
    fun onContactClick(contact: SimpleContact)

    fun onCallButtonClick(phoneNumber: String)
}