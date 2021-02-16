package com.bombadu.imager

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bombadu.imager.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import retrofit2.HttpException
import java.io.IOException
import java.nio.BufferUnderflowException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadSearchQuery()



        findViewById<TextInputEditText>(R.id.image_search_edit_text).setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val query = binding.imageSearchEditText.text.toString()
                    makeSearch(query)
                    saveSearchQuery(query)

                    true
                }

                else -> false
            }
        }

        imageAdapter.setOnItemClickListener {

            val bundle = Bundle()
            val intent = Intent(this, DetailActivity::class.java)
            bundle.putString("user", it.user)
            bundle.putString("userImage", it.userImageURL)
            bundle.putString("largeImage", it.largeImageURL)
            intent.putExtras(bundle)
            startActivity(intent)


        }



    }

    private fun loadSearchQuery() {
        val prefs = getSharedPreferences("query", MODE_PRIVATE)
        val query = prefs.getString("query", "cat")
        query?.let { makeSearch(it) }
    }


    private fun saveSearchQuery(query: String) {
        val prefs = getSharedPreferences("query", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("query", query)
        editor.apply()

    }

    private fun makeSearch(query: String) {
        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.searchForImage(query)
            } catch (e: IOException) {
                Log.e(TAG, "IOExceptions, you might not have an internet connection")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null) {
                Log.i("RESPONSE", response.body().toString())
                imageAdapter.images = response.body()!!.hits
            } else {
                Log.e(TAG, "Response not successful")
            }
            binding.progressBar.isVisible = false
        }
    }


    private fun setupRecyclerView() = binding.recyclerView.apply {
        imageAdapter = ImageAdapter()
        adapter = imageAdapter
        hasFixedSize()
        layoutManager = GridLayoutManager(this@MainActivity, 2)

    }


}