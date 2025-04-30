package com.example.graminawas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.graminawas.data.entities.Beneficiary
import com.example.graminawas.databinding.DialogAllocationBinding
import com.example.graminawas.viewmodel.BeneficiaryViewModel
import kotlinx.coroutines.launch

class AllocationDialogFragment : DialogFragment() {
    private var _binding: DialogAllocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BeneficiaryViewModel by activityViewModels()
    private lateinit var beneficiary: Beneficiary

    companion object {
        private const val ARG_BENEFICIARY = "beneficiary"

        fun newInstance(beneficiary: Beneficiary): AllocationDialogFragment {
            return AllocationDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_BENEFICIARY, beneficiary)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beneficiary = arguments?.getParcelable(ARG_BENEFICIARY)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAllocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewBeneficiaryName.text = beneficiary.name
        binding.textViewAadhar.text = "Aadhar: ${beneficiary.aadharNumber}"
        binding.textViewPhone.text = "Phone: ${beneficiary.phoneNumber}"
        binding.textViewAddress.text = "Address: ${beneficiary.address}"
        binding.textViewCurrentAllocation.text = "Current Allocation: ₹${beneficiary.fundAllocated}"

        binding.buttonAllocate.setOnClickListener {
            val amount = binding.editTextAmount.text.toString().toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    viewModel.allocateFunds(beneficiary.id, amount)
                    Toast.makeText(requireContext(), "Funds allocated successfully", Toast.LENGTH_SHORT).show()
                    dismiss()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Failed to allocate funds", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 