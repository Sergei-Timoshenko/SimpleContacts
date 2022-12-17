package dev.sergeitimoshenko.simplecontacts.ui.keypad.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sergeitimoshenko.simplecontacts.db.daos.ContactDao
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import dev.sergeitimoshenko.simplecontacts.models.mappers.ContactMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeypadViewModel @Inject constructor(
    private val contactDao: ContactDao,
    private val mapper: ContactMapper
) : ViewModel() {
    private val _phoneNumber = MutableLiveData("")
    val phoneNumber: LiveData<String> = _phoneNumber

    val contacts: LiveData<List<Contact>> = Transformations.switchMap(phoneNumber) {
        contactDao.getContactsByPhoneNumber(phoneNumber.value!!)
    }

    fun insertContact(contact: SimpleContact) =
        viewModelScope.launch(Dispatchers.IO) {
            contactDao.insertContact(mapper.fromSimpleContact(contact))
        }

    fun removeDigit() {
        _phoneNumber.value = _phoneNumber.value?.dropLast(1)
    }

    fun removeAllDigits() {
        _phoneNumber.value = ""
    }

    fun enterDigit(digit: String) {
        _phoneNumber.value = _phoneNumber.value + digit
    }
}