package com.project.dasi.galang_dana

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.project.dasi.databinding.ActivityGalangDanaBinding
import java.text.SimpleDateFormat
import com.github.dhaval2404.imagepicker.ImagePicker
import android.widget.Toast

import android.app.ProgressDialog

import com.google.firebase.storage.FirebaseStorage
import android.content.Intent

import android.app.AlertDialog
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.UploadTask
import com.project.dasi.R
import java.lang.Exception


class GalangDanaActivity : AppCompatActivity() {

    private var binding : ActivityGalangDanaBinding? = null
    private var dateStartInMillis: Long? = null
    private var dateEndInMillis: Long? = null

    private var dp: String? = null
    private val REQUEST_FROM_GALLERY = 1001

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalangDanaBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.logodasi)
            .into(binding!!.logo)


        binding?.logo?.setOnClickListener {
            onBackPressed()
        }


        binding?.registrationDonation?.setOnClickListener {
            formValidation()
        }

        /// donasi dimulai
        binding?.dateStart?.setOnClickListener {
            // Create the date picker builder and set the title
            val builder = MaterialDatePicker.Builder.datePicker().setTitleText("Pilih Tanggal Mulai").setCalendarConstraints(CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())


            // create the date picker
            val datePicker = builder.build()


            // set listener when date is selected
            datePicker.addOnPositiveButtonClickListener {
                dateStartInMillis = it
                val formatter = SimpleDateFormat("dd MMMM yyyy")
                val format: String = formatter.format(it)
                binding?.dateStart?.text = format
            }

            datePicker.show(supportFragmentManager, "MyTAG")

        }

        /// donasi selesai
        binding?.dateEnd?.setOnClickListener {
            // Create the date picker builder and set the title
            val builder = MaterialDatePicker.Builder.datePicker().setTitleText("Pilih Tanggal Selesai").setCalendarConstraints(CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())


            // create the date picker
            val datePicker = builder.build()


            // set listener when date is selected
            datePicker.addOnPositiveButtonClickListener {
                dateEndInMillis = it
                val formatter = SimpleDateFormat("dd MMMM yyyy")
                val format: String = formatter.format(it)
                binding?.dateEnd?.text = format
            }

            datePicker.show(supportFragmentManager, "MyTAG")
        }

        // KLIK TAMBAH GAMBAR
        binding?.imageTemp?.setOnClickListener{
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_FROM_GALLERY);
        }
    }

    private fun formValidation() {
        val donationName = binding?.donationName?.text.toString().trim()
        val donationOwner = binding?.donateOwner?.text.toString().trim()
        val donationTo = binding?.donateTo?.text.toString().trim()
        val donationNominal = binding?.nominal?.text.toString().trim()
        val donationDescription = binding?.donateDescription?.text.toString().trim()


        when {
            donationName.isEmpty() -> {
                Toast.makeText(this, "Nama Donasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            donationOwner.isEmpty() -> {
                Toast.makeText(this, "Nama pengaju donasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            donationNominal.isEmpty() -> {
                Toast.makeText(this, "Nominal donasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            donationTo.isEmpty() -> {
                Toast.makeText(this, "Tujuan donasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            donationDescription.isEmpty() -> {
                Toast.makeText(this, "Deskripsi donasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            dateStartInMillis == null -> {
                Toast.makeText(this, "Tanggal mulai donasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            dateEndInMillis == null -> {
                Toast.makeText(this, "Tanggal selesai donasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            dp == null -> {
                Toast.makeText(this, "Gambar donasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

        val uid = System.currentTimeMillis().toString()
        val ownerId = FirebaseAuth.getInstance().currentUser?.uid

        val data = mapOf(
            "name" to donationName,
            "owner" to donationOwner,
            "ownerId" to ownerId,
            "nominal" to donationNominal.toLong(),
            "to" to donationTo,
            "description" to donationDescription,
            "dateStart" to dateStartInMillis,
            "dateEnd" to dateEndInMillis,
            "image" to dp,
            "uid" to  uid,
            "donateValue" to  0,
        )

        FirebaseFirestore
            .getInstance()
            .collection("donation")
            .document(uid)
            .set(data)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    mProgressDialog.dismiss()
                    showSuccessDialog()
                } else {
                    mProgressDialog.dismiss()
                    showFailureDialog()
                }
            }
    }

    /// tampilkan dialog box ketika gagal mengupload
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Mengajukan Penggalangan Dana")
            .setMessage("Terdapat kesalahan ketika mengajukan penggalangan dana, silahkan periksa koneksi internet anda, dan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

    /// tampilkan dialog box ketika sukses mengupload
    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Mengajukan Penggalangan Dana")
            .setMessage("Penggalangan Dana anda akan segera terbit")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, i ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY) {
                uploadArticleDp(data?.data)
            }
        }
    }


    /// fungsi untuk mengupload foto kedalam cloud storage
    private fun uploadArticleDp(data: Uri?) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        val imageFileName = "donasi/data_" + System.currentTimeMillis() + ".png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        mProgressDialog.dismiss()
                        dp = uri.toString()
                        Glide
                            .with(this)
                            .load(dp)
                            .into(binding!!.donateImage)
                    }
                    .addOnFailureListener { e: Exception ->
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            this,
                            "Gagal mengunggah gambar",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("imageDp: ", e.toString())
                    }
            }
            .addOnFailureListener { e: Exception ->
                mProgressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Gagal mengunggah gambar",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.d("imageDp: ", e.toString())
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}