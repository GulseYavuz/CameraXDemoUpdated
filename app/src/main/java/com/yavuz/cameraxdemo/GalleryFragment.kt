package com.yavuz.cameraxdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yavuz.cameraxdemo.databinding.FragmentGalleryBinding

class GalleryFragment internal constructor(): Fragment() {
    private var _fragmentGalleryBinding: FragmentGalleryBinding? = null
    private val fragmentGalleryBinding get() = _fragmentGalleryBinding!!
    private val args: GalleryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragmentGalleryBinding = FragmentGalleryBinding.inflate(inflater, container, false)
        return fragmentGalleryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Populate the ViewPager and implement a cache of two media items

        val uri = args.savedUriArgs?.modelUri
        Toast.makeText(requireContext(), "URI: $uri", Toast.LENGTH_SHORT).show()
        Glide.with(requireContext()).load(uri).into(fragmentGalleryBinding.image)

        fragmentGalleryBinding.viewPager.apply {
            offscreenPageLimit = 2
        }
    }

    override fun onDestroyView() {
        _fragmentGalleryBinding = null
        super.onDestroyView()
    }
}