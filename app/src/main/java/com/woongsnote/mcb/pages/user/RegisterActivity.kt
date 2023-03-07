package com.woongsnote.mcb.pages.user

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.woongsnote.mcb.MainActivity
import com.woongsnote.mcb.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    lateinit var auth: FirebaseAuth
    var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
    }

    private fun initView() {
        binding.btnRegister.setOnClickListener {

            userName = binding.etId.text.toString()

            val email = binding.etEmail.text.toString()
            val password = binding.etPw.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        makeUser(task.result?.user)
                        finish()
                    } else {
                        Log.e("ERROR", "fail to make user")
                        Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun makeUser(user: FirebaseUser?) {
        Log.d("User", user.toString())
        val profileUpdate = userProfileChangeRequest {
            displayName = userName
        }
        user!!.updateProfile(profileUpdate).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User profile updated.")
                Toast.makeText(this, "계정 생성 완료", Toast.LENGTH_SHORT).show()
            }
        }
    }
}