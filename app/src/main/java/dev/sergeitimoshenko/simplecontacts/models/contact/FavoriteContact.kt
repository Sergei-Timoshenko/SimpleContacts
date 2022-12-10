package dev.sergeitimoshenko.simplecontacts.models.contact

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteContact(
    val name: String,
    val surname: String,
    val phone: String,
    val image: Bitmap? = null
) : Parcelable
