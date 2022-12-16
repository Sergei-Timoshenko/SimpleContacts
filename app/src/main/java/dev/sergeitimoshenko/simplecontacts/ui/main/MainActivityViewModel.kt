package dev.sergeitimoshenko.simplecontacts.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sergeitimoshenko.simplecontacts.db.daos.ContactDao
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {

    val contacts = contactDao.getAllContacts()

    fun insertContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactDao.insertContact(contact)
    }
}