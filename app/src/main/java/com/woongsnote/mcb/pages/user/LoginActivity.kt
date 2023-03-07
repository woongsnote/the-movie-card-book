package com.woongsnote.mcb.pages.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.woongsnote.mcb.MainActivity
import com.woongsnote.mcb.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
         setContentView(view)
        initViews()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            moveMainPage(currentUser)
        }
    }

    private fun initViews() {

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pw = binding.etPw.text.toString()
            signIn(email, pw)
        }

        binding.btnRegister.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

    }

    private fun signIn(id: String, pw: String) {

        if (id.isEmpty() && pw.isEmpty()) {
            Toast.makeText(this, "아이디, 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(id, pw)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        val intentMain = Intent(this, MainActivity::class.java)
                        startActivity(intentMain)
                    } else {
                        Toast.makeText(this, "로그인 실패!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun moveMainPage(user: FirebaseUser?){
        if(user!= null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}