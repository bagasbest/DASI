package com.project.dasi.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.dasi.databinding.ItemCurrentProjectBinding
import com.project.dasi.donasi.DonasiDetailActivity
import com.project.dasi.donasi.DonasiModel

class CurrentProjectAdapter : RecyclerView.Adapter<CurrentProjectAdapter.ViewHolder>() {

    private val donationList = ArrayList<DonasiModel>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<DonasiModel>) {
        donationList.clear()
        donationList.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrentProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(donationList[position])
    }

    override fun getItemCount(): Int = donationList.size
    inner class ViewHolder(private val binding: ItemCurrentProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DonasiModel) {
            Glide.with(itemView.context)
                .load(model.image)
                .into(binding.image)


            binding.image.setOnClickListener {
                val intent = Intent(itemView.context, DonasiDetailActivity::class.java)
                intent.putExtra(DonasiDetailActivity.EXTRA_DONASI, model)
                itemView.context.startActivity(intent)
            }
        }
    }
}