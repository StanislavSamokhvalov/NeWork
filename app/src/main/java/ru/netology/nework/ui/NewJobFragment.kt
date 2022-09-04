package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentNewJobBinding
import ru.netology.nework.util.AndroidUtils
import ru.netology.nework.viewmodel.JobViewModel

@AndroidEntryPoint
class NewJobFragment : Fragment() {

    private val jobViewModel: JobViewModel by viewModels()
    private var fragmentBinding: FragmentNewJobBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewJobBinding.inflate(
            inflater,
            container,
            false
        )
        fragmentBinding = binding

        binding.save.setOnClickListener {
            fragmentBinding?.let {
                if (it.companyName.text.isNullOrBlank() ||
                    it.textStart.text.isNullOrBlank() ||
                    it.textCompany.text.isNullOrBlank()
                ) {
                    Toast.makeText(
                        activity,
                        getString(R.string.error_create_new_job),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    jobViewModel.changeJob(
                        it.companyName.text.toString(),
                        it.myPosition.text.toString(),
                        it.start.text.toString(),
                        it.finish.text.toString()
                    )
                    jobViewModel.save()
                    AndroidUtils.hideKeyboard(requireView())
                }
            }
        }

        jobViewModel.jobCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}