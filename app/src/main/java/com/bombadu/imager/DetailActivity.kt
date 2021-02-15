package com.bombadu.imager

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bombadu.imager.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso

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
    }

    private fun getBundle() {
        val bundle = intent.extras
        largeImage = bundle?.getString("largeImage").toString()
        val userImage = bundle?.getString("userImage")
        val username = bundle?.getString("user")

        Picasso.get().load(largeImage).into(binding.imageView)
        if (userImage.isNullOrEmpty()) {
            Picasso.get().load("https://png.pngtree.com/png-vector/20190419/ourmid/pngtree-yellow-smiley-face-png-image_960884.jpg")
                .into(binding.userImageView)
        } else {
            Picasso.get().load(userImage).into(binding.userImageView)
        }

        binding.usernameTextView.text = username
    }
}