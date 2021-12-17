package com.project.dasi.donasi

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.dasi.R
import com.project.dasi.databinding.ActivityDonasiPaymentInputBinding
import java.util.*
import kotlin.concurrent.schedule

class DonasiPaymentInputActivity : AppCompatActivity() {

    private var binding: ActivityDonasiPaymentInputBinding? = null
    private lateinit var data: DonasiModel
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonasiPaymentInputBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        data = intent.getParcelableExtra<DonasiModel>(DonasiDetailActivity.EXTRA_DONASI) as DonasiModel

        Glide.with(this)
            .load(R.drawable.logodasi)
            .into(binding!!.logo)

        binding?.back?.setOnClickListener {
            onBackPressed()
        }


        binding?.donateBtn?.setOnClickListener {
            formValidation()
        }

    }

    private fun formValidation() {
        val nominal = binding?.nominal?.text.toString().trim()

        if (nominal.isEmpty()) {
            Toast.makeText(this, "Nominal donasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        } else if (nominal.contains(".") || nominal.contains(",")) {
            Toast.makeText(this, "Nominal donasi harus berupa angka saja", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

        Timer().schedule(1500) {

            /// ambil nama donatur
            val myUid = FirebaseAuth.getInstance().currentUser?.uid
            if (myUid != null) {
                FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(myUid)
                    .get()
                    .addOnSuccessListener {
                        username = "" + it["username"]

                        /// simpan riwayat donasi
                        val donateId = System.currentTimeMillis().toString()
                        val donated = mapOf(
                            "donateId" to donateId,
                            "userId" to myUid,
                            "username" to username,
                            "nominal" to nominal.toLong(),
                            "donateName" to data.name,
                            "ownerId" to data.ownerId,
                        )
                        FirebaseFirestore
                            .getInstance()
                            .collection("donatur")
                            .document(donateId)
                            .set(donated)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful) {


                                    /// update dana terkumpul pada donasi
                                    val donatedValue = data.donateValue?.plus(nominal.toLong())
                                    data.uid?.let { it1 ->
                                        FirebaseFirestore
                                            .getInstance()
                                            .collection("donation")
                                            .document(it1)
                                            .update("donateValue", donatedValue)
                                            .addOnCompleteListener { updateDonatedValue ->

                                                if(updateDonatedValue.isSuccessful) {
                                                    mProgressDialog.dismiss()
                                                    showSuccessDialog(donateId, username!!, nominal)
                                                } else {
                                                    mProgressDialog.dismiss()
                                                    showFailureDialog()
                                                }
                                            }
                                    }
                                } else {
                                    mProgressDialog.dismiss()
                                    showFailureDialog()
                                }
                            }
                    }
            }
        }

    }

    /// tampilkan dialog box ketika gagal donasi
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Melakukan Donasi")
            .setMessage("Terdapat kesalahan, Silahkan periksa koneksi internet anda, dan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

    /// tampilkan dialog box ketika sukses mendonasi
    private fun showSuccessDialog(donateId: String, username: String, nominal: String) {
        AlertDialog.Builder(this)
            .setTitle("Sukses")
            .setMessage("Berhasil melakukan donasi")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, i ->
                dialogInterface.dismiss()
                val intent = Intent(this, DonasiPaymentSuccessActivity::class.java)
                intent.putExtra(DonasiPaymentSuccessActivity.EXTRA_NOMINAL, nominal)
                intent.putExtra(DonasiPaymentSuccessActivity.EXTRA_DATE, donateId)
                intent.putExtra(DonasiPaymentSuccessActivity.EXTRA_FROM, username)
                intent.putExtra(DonasiPaymentSuccessActivity.EXTRA_TO, data.owner)
                startActivity(intent)
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    companion object {
        const val EXTRA_DONASI = "role"
    }
}