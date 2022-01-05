package com.project.dasi.donasi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.project.dasi.R
import com.project.dasi.databinding.ActivityDonasiDetailBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import androidx.constraintlayout.widget.ConstraintSet




class DonasiDetailActivity : AppCompatActivity() {


    private var binding: ActivityDonasiDetailBinding? = null
    private lateinit var data: DonasiModel


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonasiDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val formatter: NumberFormat = DecimalFormat("#,###")
        data = intent.getParcelableExtra<DonasiModel>(EXTRA_DONASI) as DonasiModel

        Glide.with(this)
            .load(R.drawable.logodasi)
            .into(binding!!.logo)

        Glide.with(this)
            .load(data.image)
            .into(binding!!.image)


        binding?.textView14?.text = data.name
        binding?.donateValue?.text = formatter.format(data.donateValue)
        binding?.nominal?.text = " / ${formatter.format(data.nominal)}"
        binding?.owner?.text = data.owner
        binding?.to?.text = "Tujuan Donasi: ${data.to}"
        binding?.description?.text = data.description

        val donateValuePercentage = data.nominal?.toDouble()
            ?.let { data.donateValue?.toDouble()?.div(it) }

        Log.e("TAG", donateValuePercentage.toString())

        if (donateValuePercentage != null) {
            if(donateValuePercentage < 0.92) {
                (binding?.progrssDonate?.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = donateValuePercentage.toFloat()
                binding?.progrssDonate!!.requestLayout()
            } else {
                (binding?.progrssDonate?.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = 0.92F
                binding?.progrssDonate!!.requestLayout()
            }
        }


        val currentTimeInMillis = System.currentTimeMillis()

        if(currentTimeInMillis < data.dateEnd!!) {
            val donationTimeLeftInMillis = data.dateEnd!! - currentTimeInMillis
            val donationTimeLeft = (donationTimeLeftInMillis / (1000*60*60*24)) % 24

            if(donationTimeLeft == 0L ){
                binding?.dayLeft?.text = "Hari Terakhir Donasi"
            } else {
                binding?.dayLeft?.text = "$donationTimeLeft Hari Lagi"
            }
        } else {
            binding?.dayLeft?.text = "Waktu Habis"
            binding?.donateBtn?.visibility = View.GONE
        }

        binding?.donateBtn?.setOnClickListener {
            val intent = Intent(this, DonasiPaymentInputActivity::class.java)
            intent.putExtra(DonasiPaymentInputActivity.EXTRA_DONASI, data)
            startActivity(intent)
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DONASI = "role"
    }
}