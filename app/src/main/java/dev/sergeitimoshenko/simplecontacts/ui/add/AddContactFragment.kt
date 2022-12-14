package dev.sergeitimoshenko.simplecontacts.ui.add

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.sergeitimoshenko.simplecontacts.R
import dev.sergeitimoshenko.simplecontacts.databinding.FragmentAddContactBinding
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import dev.sergeitimoshenko.simplecontacts.ui.add.viewmodels.AddContactViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactFragment : Fragment(R.layout.fragment_add_contact) {
    private var binding: FragmentAddContactBinding? = null
    private val viewModel: AddContactViewModel by viewModels()
    private val navArgs: AddContactFragmentArgs by navArgs()
    private var photo: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddContactBinding.bind(view)

        if (navArgs.phoneNumber != null) {
            binding?.etContactPhoneNumber?.setText(navArgs.phoneNumber)
        }

        val startImagePickerForResult =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                photo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, uri!!)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                }
                photo = getResizedBitmap(photo!!, 333)
                binding?.ivPhoto?.setImageBitmap(photo)
            }

        binding?.apply {
            ivPhoto.setOnClickListener {
                startImagePickerForResult.launch("image/*")
            }

            btnSave.setOnClickListener {
                val name = etContactName.text.toString()
                val surname = etContactSurname.text.toString()
                val phoneNumber = etContactPhoneNumber.text.toString()
                val email = etContactEmail.text.toString()
                val isFavorite = cbIsFavourite.isChecked
                val contact = Contact(
                    name, surname, phoneNumber, email, photo, isFavorite
                )

                if (name.isNotEmpty() && surname.isNotEmpty() && phoneNumber.isNotEmpty()) {
                    viewModel.insertContact(contact)
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(), "Enter name, surname and phone number", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun resizeBitmap(bitmap: Bitmap, bitmapWidth: Int, bitmapHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, true)
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}