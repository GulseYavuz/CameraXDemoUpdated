package com.yavuz.cameraxdemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.yavuz.cameraxdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val cameraFragment = CameraFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, cameraFragment).commit()

        binding.imgCaptureButton.setOnClickListener {
            cameraFragment.takePhoto()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraFragment.animateFlash()
            }
        }
        setContentView(binding.root)
    }

    fun takePhoto() {
       // Toast.makeText(this, "method called from camera fragment", Toast.LENGTH_SHORT).show()
    }

    fun animateFlash() {
       //Toast.makeText(this, "aaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show()

    }
}
