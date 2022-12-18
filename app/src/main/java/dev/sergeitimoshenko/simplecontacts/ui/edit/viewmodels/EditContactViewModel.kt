package dev.sergeitimoshenko.simplecontacts.ui.edit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sergeitimoshenko.simplecontacts.db.daos.ContactDao
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditContactViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {
    fun updateContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO + NonCancellable) {
        contactDao.updateContact(contact)
    }
}