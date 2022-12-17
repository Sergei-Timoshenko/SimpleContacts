package dev.sergeitimoshenko.simplecontacts.ui.contacts.viewmodels

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sergeitimoshenko.simplecontacts.db.daos.ContactDao
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {
    val contacts = contactDao.getAllContacts()

    fun deleteContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactDao.deleteContact(contact)
    }

    fun insertContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactDao.insertContact(contact)
    }

    fun getContacts(): List<Contact> {
        return contacts.value!!
    }
}