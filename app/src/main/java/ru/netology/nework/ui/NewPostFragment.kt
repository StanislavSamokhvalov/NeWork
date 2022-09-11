package ru.netology.nework.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentNewPostBinding
import ru.netology.nework.enumeration.AttachmentType
import ru.netology.nework.util.AndroidUtils
import ru.netology.nework.viewmodel.PostViewModel


@AndroidEntryPoint
class NewPostFragment : Fragment() {

    var type: AttachmentType? = null
    private val postViewModel: PostViewModel by activityViewModels()
    private var fragmentBinding: FragmentNewPostBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_post, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.save -> {
                        fragmentBinding?.let {
                            if (it.edit.text.isNullOrBlank()) {
                                Toast.makeText(
                                    activity,
                                    R.string.error_empty_content,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                postViewModel.changeContent(it.edit.text.toString())
                                postViewModel.save()
                                AndroidUtils.hideKeyboard(requireView())
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        fragmentBinding = binding

        binding.edit.setText(
            arguments?.getString("content") ?: postViewModel.edited.value?.content
        )
        binding.edit.requestFocus()

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                when (activityResult.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(activityResult.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val uri: Uri? = activityResult.data?.data
                        val stream = uri?.let { context?.contentResolver?.openInputStream(it) }
                        postViewModel.changeMedia(uri, stream, type)
                    }
                }
            }

        val pickMediaLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent())  { uri ->
                uri?.let {
                    val stream = context?.contentResolver?.openInputStream(it)
                    postViewModel.changeMedia(it, stream, type)
                }
            }

        binding.pickPhoto.setOnClickListener {
            pickMediaLauncher.launch("image/*")
            type = AttachmentType.IMAGE
        }

        binding.pickAudio.setOnClickListener {
            pickMediaLauncher.launch("audio/*")
            type = AttachmentType.AUDIO
        }

        binding.pickVideo.setOnClickListener {
            pickMediaLauncher.launch("video/*")
            type = AttachmentType.VIDEO
        }

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.removePhoto.setOnClickListener {
            postViewModel.changeMedia(null, null, type)
        }

        postViewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        postViewModel.media.observe(viewLifecycleOwner) {
            if (it.uri == null) {
                binding.mediaContainer.visibility = View.GONE
                return@observe
            }
            binding.mediaContainer.visibility = View.VISIBLE
            binding.photo.setImageURI(it.uri)
            binding.info.text = postViewModel.media.value?.uri?.path
        }

        return binding.root
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}
