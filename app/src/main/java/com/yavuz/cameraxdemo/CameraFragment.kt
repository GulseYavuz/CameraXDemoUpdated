package com.yavuz.cameraxdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.yavuz.cameraxdemo.databinding.FragmentCameraBinding
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {
    private var binding: FragmentCameraBinding? = null
    private val fragmentCameraBinding get() = binding!!
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private lateinit var broadcastManager: LocalBroadcastManager

    private var displayId: Int = -1
    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                startCamera()
            } else {
                Snackbar.make(
                    fragmentCameraBinding.root,
                    "The camera permission is required",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()

        // Shut down our background executor
        imgCaptureExecutor.shutdown()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return fragmentCameraBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()
        broadcastManager = LocalBroadcastManager.getInstance(view.context)
        cameraPermissionResult.launch(android.Manifest.permission.CAMERA)

        // Wait for the views to be properly laid out
        fragmentCameraBinding.preview.post {

            // Keep track of the display in which this view is attached
            displayId = fragmentCameraBinding.preview.display.displayId
            // Set up the camera and its use cases
            lifecycleScope.launch {
                startCamera()
            }
        }

        fragmentCameraBinding.buttonCapture.setOnClickListener {
            takePhoto()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                animateFlash()
            }
        }
    }

    private fun startCamera() {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(fragmentCameraBinding.preview.surfaceProvider)
        }
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed.")
            }
        }, ContextCompat.getMainExecutor(this.requireContext()))
    }

    fun takePhoto() {
        val imageCapture = imageCapture ?: return
        // Create output options object which contains file + metadata
        val fileName = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())
        val file = File(requireContext().filesDir, fileName)
        FileUri.fileUriGet = file.toUri()
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(file)
            .build()
        // Setup image capture listener which is triggered after photo has been taken
        imageCapture.takePicture(
            outputOptions, imgCaptureExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val uri = output.savedUri
                    //   val savedFile = File(savedUri?.path ?: return)
                    val bitmap = BitmapFactory.decodeFile(uri?.path)
                    /*    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray: ByteArray = stream.toByteArray()*/
                   /* val action =
                        CameraFragmentDirections.actionCameraFragmentToGalleryFragment()*/
                    val args = Bundle()
                    args.putString("uri", bitmap.toString())
                    requireActivity().runOnUiThread {
                        findNavController().navigate(R.id.action_cameraFragment_to_galleryFragment, args)
                        }

                  /*  findNavController()
                        .navigate(R.id.action_cameraFragment_to_galleryFragment, args)*/

                        //    Log.d(TAG, "Photo capture succeeded: $savedUri")

                        /*            binding?.buttonPhotoView?.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putParcelable("bitmap", bitmap)
                        val action = CameraFragmentDirections.actionCameraFragmentToGalleryFragment()
                        action.arguments = bundle
                        // fragment.setArguments(bundle)

                        requireActivity().runOnUiThread {
                            findNavController().navigate(action)
                        }
                    }*/




/*        fragmentCameraBinding.buttonPhotoView.setOnClickListener {
            // Only navigate when the gallery has photos
            lifecycleScope.launch {
                val savedUriPath =file.toString()
              *//*  val bundle = Bundle()
                bundle.putParcelable("bitmap", bitmap)
                fragment.setArguments(bundle)*//*

           *//*     requireActivity().runOnUiThread {
                    findNavController().navigate(action)

                    *//**//*   Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                        .navigate(CameraFragmentDirections.actionCameraFragmentToPhotoFragment(
                            mediaStoreUtils.mediaStoreCollection.toString()
                        )
                        )*//**//*
                }*//*
            }
        }*/
    }
            })
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun animateFlash(){
        fragmentCameraBinding.root.postDelayed({
            fragmentCameraBinding.root.foreground = ColorDrawable(Color.WHITE)
            fragmentCameraBinding.root.postDelayed({
                fragmentCameraBinding.root.foreground = null
            }, 50)
        }, 100)
    }
    companion object{
        private const val TAG = "CameraFragment"
    }
}
