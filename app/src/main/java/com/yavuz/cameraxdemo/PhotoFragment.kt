/*

package com.yavuz.cameraxdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yavuz.cameraxdemo.databinding.PhotoFragmentBinding

*/
/** Fragment used for each individual page showing a photo inside of [GalleryFragment] *//*

class PhotoFragment internal constructor() : Fragment() {
    private var binding: PhotoFragmentBinding? = null
    private val fragmentPhotoBinding get() = binding!!
    private val args: PhotoFragmentArgs by navArgs()

    */
/** AndroidX navigation arguments *//*

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PhotoFragmentBinding.inflate(inflater, container, false)
       // PhotoFragmentArgs(args.savedUriArgs)
        return fragmentPhotoBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val savedUri = args.savedUriArgs
        //val resource = arguments?.getString(FILE_NAME_KEY)
        val bundle = bundleOf()
        fragmentPhotoBinding.let { Glide.with(requireContext()).load(FileUri.fileUriGet).into(it.imageView) }
        fragmentPhotoBinding.buttonSave.setOnClickListener {
            requireActivity().runOnUiThread {
                findNavController().navigate(R.id.action_photoFragment_to_galleryFragment)
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
    companion object {
        private const val FILE_NAME_KEY = "file_name"

  */
/*      fun create(mediaStoreFile: MediaStoreFile) = PhotoFragment().apply {
            val image = mediaStoreFile.file
            arguments = Bundle().apply {
                putString(FILE_NAME_KEY, image.absolutePath)
            }
        }*//*

    }
}*/
