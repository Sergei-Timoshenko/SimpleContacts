package dev.sergeitimoshenko.simplecontacts.models.mappers

import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import javax.inject.Inject

class ContactMapper @Inject constructor() {
    fun fromSimpleContact(contact: SimpleContact): Contact = Contact(
        name = contact.name,
        surname = contact.surname,
        phoneNumber = contact.phoneNumber,
        id = contact.id
    )

    fun toSimpleContact(contact: Contact): SimpleContact = SimpleContact(
        name = contact.name,
        surname = contact.surname,
        phoneNumber = contact.phoneNumber,
        image = contact.image,
        id = contact.id
    )

    fun toSimpleContactList(contacts: List<Contact>?): List<SimpleContact>? {
        return contacts?.map { contact -> toSimpleContact(contact) }
    }
}