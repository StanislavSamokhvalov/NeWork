package ru.netology.nework.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nework.R
import ru.netology.nework.adapter.EventAdapter
import ru.netology.nework.adapter.EventCallback
import ru.netology.nework.databinding.FragmentEventsBinding
import ru.netology.nework.dto.Event
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.EventViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EventsFragment : Fragment() {

    private val eventViewModel: EventViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)

        val adapter = EventAdapter(object : EventCallback {
            override fun onLike(event: Event) {
                if (authViewModel.authenticated) {
                    if (!event.likedByMe) eventViewModel.likeById(event.id) else eventViewModel.unlikeById(
                        event.id
                    )
                } else findNavController().navigate(R.id.action_navigation_events_to_signInFragment)
            }

            override fun onRemove(event: Event) {
                eventViewModel.removeById(event.id)
            }

            override fun onShare(event: Event) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, event.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_event))
                startActivity(shareIntent)

            }

            override fun onEdit(event: Event) {
                eventViewModel.edit(event)
            }

            override fun onJoin(event: Event) {
                if (!event.participatedByMe) eventViewModel.joinById(event.id)
                else eventViewModel.unJoinById(event.id)
            }

            override fun onParty(event: Event) {
//                eventViewModel.partyById(event.id)
                //navigate to bottom shield fragment
            }

            override fun onSpeakers(event: Event) {
                super.onSpeakers(event)
            }

        })

        binding.list.adapter = adapter

        lifecycleScope.launchWhenCreated {
            eventViewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }

        return binding.root
    }
}