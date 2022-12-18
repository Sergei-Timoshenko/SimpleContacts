package dev.sergeitimoshenko.simplecontacts.ui.keypad

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dev.sergeitimoshenko.simplecontacts.R
import dev.sergeitimoshenko.simplecontacts.databinding.FragmentKeypadBinding
import dev.sergeitimoshenko.simplecontacts.models.contact.Contact
import dev.sergeitimoshenko.simplecontacts.models.contact.SimpleContact
import dev.sergeitimoshenko.simplecontacts.models.mappers.ContactMapper
import dev.sergeitimoshenko.simplecontacts.ui.keypad.adapters.KeypadAdapter
import dev.sergeitimoshenko.simplecontacts.ui.keypad.listeners.OnActionsClickListener
import dev.sergeitimoshenko.simplecontacts.ui.keypad.listeners.OnContactClickListener
import dev.sergeitimoshenko.simplecontacts.ui.keypad.viewmodels.KeypadViewModel
import dev.sergeitimoshenko.simplecontacts.utils.REQUEST_CALL
import javax.inject.Inject

@AndroidEntryPoint
class KeypadFragment : Fragment(R.layout.fragment_keypad), OnActionsClickListener,
    OnContactClickListener {
    private val viewModel: KeypadViewModel by viewModels()
    private var binding: FragmentKeypadBinding? = null
    private lateinit var keypadAdapter: KeypadAdapter

    @Inject
    lateinit var mapper: ContactMapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentKeypadBinding.bind(view)

        // Setup buttons
        setupButtons()
        binding?.btnCall?.setOnClickListener {
            if (viewModel.phoneNumber.value != "") {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL
                    )
                } else {
                    val callIntent = Intent(
                        Intent.ACTION_CALL, Uri.parse("tel:" + viewModel.phoneNumber.value)
                    )
                    startActivity(callIntent)
                }
            }
        }

        // Hide bottom navigation view
        hideBottomNavigationView()

        // Recycler view setup
        keypadAdapter = KeypadAdapter(
            this@KeypadFragment, this@KeypadFragment
        )
        setupRecyclerView()

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            var isContactExist = false
            for (contact in contacts) {
                if (contact.phoneNumber == viewModel.phoneNumber.value) {
                    isContactExist = true
                    break
                }
            }

            if (contacts.isEmpty() && viewModel.phoneNumber.value?.isNotEmpty()!!) {
                keypadAdapter.differ.submitList(listOf("666"))
            } else if (contacts.isNotEmpty() && isContactExist && viewModel.phoneNumber.value?.isNotEmpty()!!) {
                keypadAdapter.differ.submitList(mapper.toSimpleContactList(contacts))
            } else if (contacts.isNotEmpty() && viewModel.phoneNumber.value?.isNotEmpty()!!) {
                val contactsWithActions = mutableListOf<Any>()
                contactsWithActions.addAll(mapper.toSimpleContactList(contacts)!!)
                contactsWithActions.add("666")

                keypadAdapter.differ.submitList(contactsWithActions.toList())
            } else {
                keypadAdapter.differ.submitList(listOf())
            }
        }

        viewModel.phoneNumber.observe(viewLifecycleOwner) { phoneNumber ->
            if (phoneNumber.isEmpty()) {
                binding?.ibtnRemove?.visibility = View.GONE
            } else {
                binding?.ibtnRemove?.visibility = View.VISIBLE
            }

            binding?.tvPhoneNumber?.text = phoneNumber
        }
    }

    private fun setupRecyclerView() {
        binding?.rvKeypad?.apply {
            adapter = keypadAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupButtons() {
        binding?.apply {
            btnEnterDigitOne.setOnClickListener {
                viewModel.enterDigit("1")
            }

            btnEnterDigitTwo.setOnClickListener {
                viewModel.enterDigit("2")
            }

            btnEnterDigitThree.setOnClickListener {
                viewModel.enterDigit("3")
            }

            btnEnterDigitFour.setOnClickListener {
                viewModel.enterDigit("4")
            }

            btnEnterDigitFive.setOnClickListener {
                viewModel.enterDigit("5")
            }

            btnEnterDigitSix.setOnClickListener {
                viewModel.enterDigit("6")
            }

            btnEnterDigitSeven.setOnClickListener {
                viewModel.enterDigit("7")
            }

            btnEnterDigitEight.setOnClickListener {
                viewModel.enterDigit("8")
            }

            btnEnterDigitNine.setOnClickListener {
                viewModel.enterDigit("9")
            }

            btnEnterDigitZero.setOnClickListener {
                viewModel.enterDigit("0")
            }

            btnEnterDigitZero.setOnLongClickListener {
                viewModel.enterDigit("+")
                true
            }

            btnEnterHash.setOnClickListener {
                viewModel.enterDigit("#")
            }

            btnEnterAsterisk.setOnClickListener {
                viewModel.enterDigit("*")
            }

            ibtnRemove.setOnClickListener {
                viewModel.removeDigit()
            }

            ibtnRemove.setOnLongClickListener {
                viewModel.removeAllDigits()
                true
            }
        }
    }

    private fun hideBottomNavigationView() {
        activity?.findViewById<BottomNavigationView>(R.id.bnv_main)?.visibility = View.GONE
    }

    override fun onContactClick(contact: SimpleContact) {
        // TODO
    }

    override fun onCallButtonClick(phoneNumber: String) {
        if (viewModel.phoneNumber.value != "") {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL
                )
            } else {
                val callIntent = Intent(
                    Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber")
                )
                startActivity(callIntent)
            }
        }
    }

    override fun onAddContactClick() {
        val action = KeypadFragmentDirections.actionKeypadFragmentToAddContactFragment(viewModel.phoneNumber.value)
        findNavController().navigate(action)
    }

    override fun onSmsContactClick() {
        val smsIntent =
            Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${viewModel.phoneNumber.value}"))
        startActivity(smsIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

        // Show bottom navigation view
        activity?.findViewById<BottomNavigationView>(R.id.bnv_main)?.visibility = View.VISIBLE
    }
}