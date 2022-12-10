package dev.sergeitimoshenko.simplecontacts.models.mappers

import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import dev.sergeitimoshenko.simplecontacts.models.contact.FavoriteContact
import javax.inject.Inject

class ContactMapper @Inject constructor() {
    fun toFavoriteContact(contact: Contact): FavoriteContact =
        FavoriteContact(
            name = contact.name,
            surname = contact.surname,
            phone = contact.phone,
            image = contact.image
        )
}