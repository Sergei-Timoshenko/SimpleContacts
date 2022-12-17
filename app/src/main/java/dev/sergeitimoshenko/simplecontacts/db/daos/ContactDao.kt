package dev.sergeitimoshenko.simplecontacts.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact_table")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact_table WHERE contact_phone_number LIKE '%' || :phoneNumber || '%'")
    fun getContactsByPhoneNumber(phoneNumber: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contact_table WHERE contact_phone_number LIKE '%' || :query || '%' OR contact_name LIKE '%' || :query || '%' OR contact_surname LIKE '%' || :query || '%'")
    fun getContactsByQuery(query: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contact_table WHERE contact_phone_number LIKE '%' || :query || '%' OR contact_name LIKE '%' || :query || '%' OR contact_surname LIKE '%' || :query || '%'")
    fun getFavoriteContactsByQuery(query: String): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)
}