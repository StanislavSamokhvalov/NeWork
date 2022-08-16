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
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nework.R
import ru.netology.nework.adapter.PostCallback
import ru.netology.nework.adapter.PostsAdapter
import ru.netology.nework.databinding.FragmentPostsBinding
import ru.netology.nework.dto.Post
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.PostViewModel

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private val postViewModel: PostViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostsBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : PostCallback {

            override fun onEdit(post: Post) {
                postViewModel.edit(post)
                val bundle = Bundle().apply {
                    putString("content", post.content)
                }
                findNavController().navigate(R.id.newPostFragment, bundle)
            }

            override fun onLike(post: Post) {
                if (authViewModel.authenticated) {
                    if (!post.likedByMe) postViewModel.likeById(post.id) else postViewModel.unlikeById(
                        post.id
                    )
                } else findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
            }

            override fun onRemove(post: Post) {
                postViewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onHide(post: Post) {
                postViewModel.hidePost(post)
            }

            override fun onAudio(post: Post) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.attachment?.url))
                    val audioIntent =
                        Intent.createChooser(intent, getString(R.string.media_chooser))
                    startActivity(audioIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_play_audio, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onVideo(post: Post) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.attachment?.url))
                    val videoIntent =
                        Intent.createChooser(intent, getString(R.string.media_chooser))
                    startActivity(videoIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_play_video, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onOpenImage(post: Post) {
                val bundle = Bundle().apply {
                    putString("url", post.attachment?.url)
                }
                findNavController().navigate(R.id.singleImageFragment, bundle)
            }

            override fun onOpenAvatar(post: Post) {
                val bundle = Bundle().apply {
                    putString("url", post.authorAvatar)
                }
                findNavController().navigate(R.id.singleImageFragment, bundle)
            }
        })

        binding.list.adapter = adapter

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_posts_to_newPostFragment)
        }

        binding.swiperefresh.setOnRefreshListener {
            postViewModel.refreshPosts()
        }

        postViewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG).show()
            }
        }

        lifecycleScope.launchWhenCreated {
            postViewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }

        return binding.root
    }
}