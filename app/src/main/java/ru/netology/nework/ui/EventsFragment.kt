package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.adapter.EventAdapter
import ru.netology.nework.adapter.OnInteractionListener
import ru.netology.nework.adapter.PostsAdapter
import ru.netology.nework.databinding.FragmentEventsBinding
import ru.netology.nework.viewmodel.EventViewModel

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)

        val adapter = EventAdapter(object : OnInteractionListener {})

        binding.list.adapter = adapter

        return binding.root
    }
}