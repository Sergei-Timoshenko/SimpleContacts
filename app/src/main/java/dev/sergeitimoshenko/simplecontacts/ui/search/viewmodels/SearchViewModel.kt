package dev.sergeitimoshenko.simplecontacts.ui.search.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sergeitimoshenko.simplecontacts.db.daos.ContactDao
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {
    private val _query = MutableLiveData<String>("")
    val query: LiveData<String> = _query

    val contacts: LiveData<List<Contact>> = Transformations.switchMap(query) {
        contactDao.getContactsByQuery(query.value!!)
    }

    fun setSearchQuery(query: String) {
        _query.value = query
    }
}