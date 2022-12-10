package dev.sergeitimoshenko.simplecontacts.models.contact

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "contact_table")
@Parcelize
data class Contact(
    @ColumnInfo(name = "contact_name")
    val name: String,

    @ColumnInfo(name = "contact_surname")
    val surname: String,

    @ColumnInfo(name = "contact_phone")
    val phone: String,

    @ColumnInfo(name = "contact_email")
    val email: String? = null,

    @ColumnInfo(name = "contact_image")
    val image: Bitmap? = null,

    @ColumnInfo(name = "contact_is_favorite")
    val isFavorite: Boolean = false,

    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
) : Parcelable
