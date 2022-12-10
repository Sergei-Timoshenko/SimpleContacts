package dev.sergeitimoshenko.simplecontacts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sergeitimoshenko.simplecontacts.db.converters.ImageConverter
import dev.sergeitimoshenko.simplecontacts.db.daos.ContactsDao
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact

@Database(
    entities = [Contact::class],
    version = 1
)
@TypeConverters(ImageConverter::class)
abstract class ContactsDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "contacts_database"
    }

    abstract fun getContactsDao(): ContactsDao
}