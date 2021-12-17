package com.project.dasi.donasi

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.dasi.databinding.ActivityDonasiPaymentSuccessBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import android.content.Intent
import com.bumptech.glide.Glide
import com.project.dasi.R
import com.project.dasi.homepage.HomeActivity


class DonasiPaymentSuccessActivity : AppCompatActivity() {

    private var binding: ActivityDonasiPaymentSuccessBinding? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonasiPaymentSuccessBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.logodasi)
            .into(binding!!.logo)

        val nominalCurrency: NumberFormat = DecimalFormat("#,###")
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val format: String = formatter.format(intent.getStringExtra(EXTRA_DATE)?.toLong())

        binding?.date?.setText(format)
        binding?.from?.setText(intent.getStringExtra(EXTRA_FROM))
        binding?.to?.setText(intent.getStringExtra(EXTRA_TO))
        binding?.nominal?.setText(nominalCurrency.format(intent.getStringExtra(EXTRA_NOMINAL)?.toLong()))

        binding?.back?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_FROM = "from"
        const val EXTRA_TO = "to"
        const val EXTRA_NOMINAL = "nominal"
        const val EXTRA_DATE = "date"
    }
}

