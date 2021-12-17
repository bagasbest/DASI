package com.project.dasi.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.dasi.R
import com.project.dasi.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private var binding: ActivityProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        Glide.with(this)
            .load(R.drawable.logodasi)
            .into(binding!!.logo)


        getUserProfileFromDatabase()



        binding?.editBtn?.setOnClickListener {

        }

    }

    private fun getUserProfileFromDatabase() {
        val myUid = FirebaseAuth.getInstance().currentUser?.uid
        if (myUid != null) {
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(myUid)
                .get()
                .addOnSuccessListener { document ->
                    val dp = document.data?.get("dp")?.toString()
                    val username = document.data?.get("username")?.toString()
                    val address = document.data?.get("address")?.toString()

                    if(dp != "") {
                        binding?.let {
                            Glide.with(this)
                                .load(dp)
                                .into(it.dp)
                        }
                    }

                    binding?.username?.text = username
                    binding?.location?.text = address


                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}