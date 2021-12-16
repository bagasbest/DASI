package com.project.dasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.project.dasi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.logodasi)
            .into(binding!!.logo)



    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}