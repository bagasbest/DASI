package com.project.dasi.donasi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.project.dasi.R
import com.project.dasi.databinding.ActivityDonasiDetailBinding
import java.text.DecimalFormat
import java.text.NumberFormat

class DonasiDetailActivity : AppCompatActivity() {


    private var binding: ActivityDonasiDetailBinding? = null
    private lateinit var data: DonasiModel


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonasiDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val formatter: NumberFormat = DecimalFormat("#,###")
        val myUid = FirebaseAuth.getInstance().currentUser?.uid
        data = intent.getParcelableExtra<DonasiModel>(EXTRA_DONASI) as DonasiModel

        Glide.with(this)
            .load(R.drawable.logodasi)
            .into(binding!!.logo)

        Glide.with(this)
            .load(data.image)
            .into(binding!!.image)


        binding?.textView14?.text = data.name
        binding?.donateValue?.text = formatter.format(data.donateValue)
        binding?.nominal?.text = formatter.format(data.nominal)
        binding?.owner?.text = data.owner
        binding?.to?.text = "Tujuan Donasi: ${data.to}"
        binding?.to?.text = data.description

        val currentTimeInMillis = System.currentTimeMillis()
        if(currentTimeInMillis < data.dateEnd!!) {
            val donationTimeLeftInMillis = data.dateEnd!! - currentTimeInMillis
            val donationTimeLeft = (donationTimeLeftInMillis / (1000*60*60)) % 24

            binding?.dayLeft?.text = "$donationTimeLeft Hari Lagi"
        } else {
            binding?.dayLeft?.text = "Waktu Habis"
            binding?.donateBtn?.visibility = View.GONE
        }

        if(myUid == data.ownerId) {
            binding?.deleteDonate?.visibility = View.VISIBLE
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