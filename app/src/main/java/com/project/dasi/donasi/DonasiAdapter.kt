package com.project.dasi.donasi

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.dasi.databinding.ItemDonateBinding
import java.text.DecimalFormat
import java.text.NumberFormat

class DonasiAdapter : RecyclerView.Adapter<DonasiAdapter.ViewHolder>() {

    private val donationList = ArrayList<DonasiModel>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<DonasiModel>) {
        donationList.clear()
        donationList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDonateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(donationList[position])
    }

    override fun getItemCount(): Int = donationList.size

    inner class ViewHolder(private val binding: ItemDonateBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: DonasiModel) {
            with(binding) {
                val currentTimeInMillis = System.currentTimeMillis()
                val formatter: NumberFormat = DecimalFormat("#,###")

                Glide.with(itemView.context)
                    .load(model.image)
                    .into(binding.roundedImageView)

                textView12.text = model.name
                donateValue.text = "Rp ${formatter.format(model.donateValue)}"


                val donateValuePercentage = model.nominal?.toDouble()
                    ?.let { model.donateValue?.toDouble()?.div(it) }

                if (donateValuePercentage != null) {
                    if(donateValuePercentage < 0.95) {
                        (binding.view9.layoutParams as ConstraintLayout.LayoutParams)
                            .matchConstraintPercentWidth = donateValuePercentage.toFloat()
                        binding.view9.requestLayout()
                    } else {
                        (binding.view9.layoutParams as ConstraintLayout.LayoutParams)
                            .matchConstraintPercentWidth = 0.95F
                        binding.view9.requestLayout()
                    }
                }

                if(currentTimeInMillis < model.dateEnd!!) {
                    val donationTimeLeftInMillis = model.dateEnd!! - currentTimeInMillis
                    val donationTimeLeft = (donationTimeLeftInMillis / (1000*60*60*24)) % 24

                    if(donationTimeLeft == 0L) {
                        donateDayLeft.text = "Hari Terakhir Donasi"
                    } else {
                        donateDayLeft.text = "$donationTimeLeft Hari Lagi"
                    }

                } else {
                    donateDayLeft.text = "Waktu Habis"
                }

                view7.setOnClickListener {
                    val intent = Intent(itemView.context, DonasiDetailActivity::class.java)
                    intent.putExtra(DonasiDetailActivity.EXTRA_DONASI, model)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }
}