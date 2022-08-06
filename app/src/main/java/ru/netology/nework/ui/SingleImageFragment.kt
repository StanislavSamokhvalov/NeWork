package ru.netology.nework.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentSingleImageBinding

@AndroidEntryPoint
class SingleImageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //change color
        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            val statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.primaryDarkColor)
            activity.window.statusBarColor = statusBarColor
            activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(statusBarColor))
        }

        val binding = FragmentSingleImageBinding.inflate(inflater, container, false)

        val url = arguments?.getString("url")

        binding.apply {
            Glide.with(picture)
                .load(url)
                .placeholder(R.drawable.ic_image_placeholder_150dp)
                .timeout(10000)
                .error(R.drawable.ic_error_placeholder_150dp)
                .into(picture)
        }

        binding.picture.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //return color
        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            val statusBarColor = ContextCompat.getColor(
                requireActivity(),
                R.color.primaryDarkColor
            )
            activity.window.statusBarColor = statusBarColor
            activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(statusBarColor))
        }
    }
}