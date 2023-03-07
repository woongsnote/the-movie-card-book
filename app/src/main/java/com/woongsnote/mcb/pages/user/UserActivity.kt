package com.woongsnote.mcb.pages.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.woongsnote.mcb.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvUserId.text = auth.currentUser?.displayName
        binding.tvUserEmail.text = auth.currentUser?.email

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}