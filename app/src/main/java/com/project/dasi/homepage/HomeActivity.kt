package com.project.dasi.homepage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.project.dasi.R
import com.project.dasi.databinding.ActivityHomeBinding
import com.project.dasi.donasi.DonasiActivity
import com.project.dasi.donasi.DonasiAdapter
import com.project.dasi.donasi.DonasiViewModel
import com.project.dasi.galang_dana.GalangDanaActivity
import com.project.dasi.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null
    private var adapter: DonasiAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecyclerView()
        initViewModel()

        Glide.with(this)
            .load(R.drawable.logodasi)
            .into(binding!!.imageView)


        Glide.with(this)
            .load(R.drawable.donasi)
            .into(binding!!.donate)

        Glide.with(this)
            .load(R.drawable.galang_dana)
            .into(binding!!.galangDana)

        Glide.with(this)
            .load(R.drawable.profile)
            .into(binding!!.profile)

        Glide.with(this)
            .load(R.drawable.banner_galang_dana)
            .into(binding!!.banner)


        binding?.donateNow?.setOnClickListener {
            startActivity(Intent(this, DonasiActivity::class.java))
        }

        binding?.view4?.setOnClickListener {
            startActivity(Intent(this, DonasiActivity::class.java))
        }

        binding?.view6?.setOnClickListener {
            startActivity(Intent(this, GalangDanaActivity::class.java))
        }

        binding?.view5?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }




    }


    private fun initRecyclerView() {
        binding?.rvDonate?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapter = DonasiAdapter()
        binding?.rvDonate?.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[DonasiViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListDonate()
        viewModel.getDonateList().observe(this) { alarm ->
            if (alarm.size > 0) {
                binding!!.noData.visibility = View.GONE
                adapter!!.setData(alarm)
            } else {
                binding!!.noData.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}