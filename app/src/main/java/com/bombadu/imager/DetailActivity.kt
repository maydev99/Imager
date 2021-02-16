package com.bombadu.imager

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bombadu.imager.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import java.io.File

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var largeImage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundle()

        binding.webImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(largeImage)
            startActivity(intent)
        }

        binding.downloadImageView.setOnClickListener {
            saveImage()
        }

        binding.shareImageView.setOnClickListener {
            shareImageUrl(largeImage)
        }
    }

    private fun shareImageUrl(url: String) {
        val intent = Intent(Intent.ACTION_SEND)

        intent.putExtra(Intent.EXTRA_TEXT, "Url for  Image\n$url")
        intent.type = "text/plain"
        startActivity(intent)
    }

    private fun saveImage() {
        val iv = binding.imageView
        val bitmap = (iv.drawable as BitmapDrawable).bitmap

        Toast.makeText(this, "Image NOT saved", Toast.LENGTH_SHORT).show()


    }

    private fun getTimeStamp(): String {
        return System.currentTimeMillis().toString()
    }

    private fun getBundle() {
        val bundle = intent.extras
        largeImage = bundle?.getString("largeImage").toString()
        val userImage = bundle?.getString("userImage")
        val username = bundle?.getString("user")

        Picasso.get().load(largeImage).into(binding.imageView)
        if (userImage.isNullOrEmpty()) {
            Picasso.get()
                .load("https://lh3.googleusercontent.com/proxy/yMy9op1shOcqPqeR9tBjUIyXES8XPeDu-RX6wLChJSgiWl8jgI2ca9ds4uRmz_1oE9omrLKnbEcklcOKubn95q_k6UBkIxOsqO4lo0LJObeWWV6FOzj6pNnZt-8JDWc")
                .into(binding.userImageView)
        } else {
            Picasso.get().load(userImage).into(binding.userImageView)
        }

        binding.usernameTextView.text = username
    }


}