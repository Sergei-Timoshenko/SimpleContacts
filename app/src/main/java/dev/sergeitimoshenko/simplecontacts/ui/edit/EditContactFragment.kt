package dev.sergeitimoshenko.simplecontacts.ui.edit

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.sergeitimoshenko.simplecontacts.R
import dev.sergeitimoshenko.simplecontacts.databinding.FragmentEditContactBinding
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import dev.sergeitimoshenko.simplecontacts.ui.edit.viewmodels.EditContactViewModel

@AndroidEntryPoint
class EditContactFragment : Fragment(R.layout.fragment_edit_contact) {
    private var binding: FragmentEditContactBinding? = null
    private val viewModel: EditContactViewModel by viewModels()
    private val navArgs: EditContactFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditContactBinding.bind(view)
        var photo = navArgs.contact.photo

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
            ivPhoto.setImageBitmap(navArgs.contact.photo)

            ivPhoto.setOnClickListener {
                startImagePickerForResult.launch("image/*")
            }

            btnSave.setOnClickListener {
                viewModel.updateContact(Contact("Is", "Updated", "666", "666", photo, false, navArgs.contact.id))
                findNavController().popBackStack()
            }
        }
    }

    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
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