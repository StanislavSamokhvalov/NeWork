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
import ru.netology.nework.databinding.FragmentNewEventBinding
import ru.netology.nework.dto.Event
import ru.netology.nework.util.AndroidUtils.formatDateTime
import ru.netology.nework.util.AndroidUtils.formatToDate
import ru.netology.nework.viewmodel.EventViewModel

@AndroidEntryPoint
class NewEventFragment : Fragment() {

    private val eventViewModel: EventViewModel by viewModels()
    private var fragmentBinding: FragmentNewEventBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewEventBinding.inflate(
            inflater,
            container,
            false
        )

        fragmentBinding = binding

        val dateTime = arguments?.getString("dateTime")?.let { formatToDate(it) }
            ?: formatToDate("${eventViewModel.edited.value?.datetime}")

        val date = dateTime.substring(0, 10)
        val time = dateTime.substring(11)

        binding.editText.setText(
            arguments?.getString("content") ?: eventViewModel.edited.value?.content
        )
        if (eventViewModel.edited.value != Event.empty) {
            binding.editDate.setText(date)
            binding.editTime.setText(time)
        }

        binding.ok.setOnClickListener {
            fragmentBinding?.let {
                if (it.editText.text.isNullOrBlank()) {
                    Toast.makeText(
                        activity,
                        R.string.error_empty_content,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    eventViewModel.changeContent(
                        it.editText.text.toString(),
                        formatDateTime("${it.editDate.text} ${it.editTime.text}"),
                    )
                    eventViewModel.save()
                }
            }
        }

        eventViewModel.eventCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}

