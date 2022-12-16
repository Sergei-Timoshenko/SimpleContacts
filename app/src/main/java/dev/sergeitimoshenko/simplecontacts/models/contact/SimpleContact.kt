package dev.sergeitimoshenko.simplecontacts.models.contact

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimpleContact(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val image: Bitmap? = null,
    val id: String
) : Parcelable
