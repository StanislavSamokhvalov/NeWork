package ru.netology.nework.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
import ru.netology.nework.viewmodel.UserViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EventsFragment : Fragment() {

    private val eventViewModel: EventViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()

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

            override fun onVideo(event: Event) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.attachment?.url))
                    val videoIntent =
                        Intent.createChooser(intent, getString(R.string.media_chooser))
                    startActivity(videoIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_play_video, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAudio(event: Event) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.attachment?.url))
                    val audioIntent =
                        Intent.createChooser(intent, getString(R.string.media_chooser))
                    startActivity(audioIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_play_audio, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onEdit(event: Event) {
                eventViewModel.edit(event)
                val bundle = Bundle().apply {
                    putString("content", event.content)
                    putString("dateTime", event.datetime)
                }
                findNavController().navigate(R.id.fragmentNewEvent, bundle)
            }

            override fun onJoin(event: Event) {
                if (authViewModel.authenticated) {
                    if (!event.participatedByMe) eventViewModel.joinById(event.id)
                    else eventViewModel.unJoinById(event.id)
                } else findNavController().navigate(R.id.action_navigation_events_to_signInFragment)
            }

            override fun onParty(event: Event) {
                if (authViewModel.authenticated) {
                    userViewModel.getUsersIds(event.participantsIds)
                    if (event.participantsIds.isEmpty()) {
                        Toast.makeText(context, R.string.empty_participants, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        findNavController().navigate(R.id.action_navigation_events_to_bottomSheetFragment)
                    }
                } else findNavController().navigate(R.id.action_navigation_events_to_signInFragment)
            }

            override fun onSpeakers(event: Event) {
                if (authViewModel.authenticated) {
                    userViewModel.getUsersIds(event.speakerIds)
                    if (event.speakerIds.isEmpty()) {
                        Toast.makeText(context, R.string.empty_speakers, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        findNavController().navigate(R.id.action_navigation_events_to_bottomSheetFragment)
                    }
                } else findNavController().navigate(R.id.action_navigation_events_to_signInFragment)
            }

            override fun onLikeOwners(event: Event) {
                if (authViewModel.authenticated) {
                    userViewModel.getUsersIds(event.likeOwnerIds)
                    if (event.likeOwnerIds.isEmpty()) {
                        Toast.makeText(context, R.string.empty_like_owners, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        findNavController().navigate(R.id.action_navigation_events_to_bottomSheetFragment)
                    }
                } else findNavController().navigate(R.id.action_navigation_events_to_signInFragment)
            }//TODO("ADD LIKE OWNER BUTTON")

            override fun onOpenAvatar(event: Event) {
                val bundle = Bundle().apply {
                    putString("url", event.authorAvatar)
                }
                findNavController().navigate(R.id.singleImageFragment, bundle)
            }

        })

        binding.list.adapter = adapter

        lifecycleScope.launchWhenCreated {
            eventViewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }

        eventViewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG).show()
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_events_to_fragmentNewEvent)
        }

        binding.swiperefresh.setOnRefreshListener {
            eventViewModel.refreshEvents()
        }

        return binding.root
    }
}