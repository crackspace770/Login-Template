package com.example.logintemplate.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logintemplate.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        playAnimation()
        setupView()
        register()
    }

    private fun register() {

        binding.signupButton.setOnClickListener {
            if (TextUtils.isEmpty(binding.usernameEditText.text.toString())) {
                binding.usernameEditText.setError("Mohon masukkan nama pengguna! ")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(binding.nameEditText.text.toString())) {
                binding.nameEditText.setError("Mohon masukkan nama! ")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(binding.emailEditText.text.toString())) {
                binding.emailEditText.setError("Please enter user email")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(binding.passwordEditText.text.toString())) {
                binding.passwordEditText.setError("Please enter password ")
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(

                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString(),


            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
                        currentUSerDb?.child("username")
                            ?.setValue(binding.usernameEditText.text.toString())
                        currentUSerDb?.child("name")?.setValue(binding.nameEditText.text.toString())

                        Toast.makeText(
                            this@SignupActivity,
                            "Berhasil mendaftar, silahkan lanjut login ",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()

                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            "Gagal mendaftar, silahkan coba lagi! ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        binding.backToLogin.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

 //   private fun showLoading(isLoading: Boolean) {
 //       if (isLoading) binding.progressBar.visibility = View.VISIBLE
 ////       else binding.progressBar.visibility = View.GONE
//    }

    private fun playAnimation() {
   //     ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
   //         duration = 6000
 //           repeatCount = ObjectAnimator.INFINITE
 //           repeatMode = ObjectAnimator.REVERSE
 //       }.start()

        val title = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val usernameTextView =
            ObjectAnimator.ofFloat(binding.usernameTextView, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                usernameTextView,
                emailTextView,
                passwordTextView,
                signup
            )
            startDelay = 500
        }.start()
    }

}