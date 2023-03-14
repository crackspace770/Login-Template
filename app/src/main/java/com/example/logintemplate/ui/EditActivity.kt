package com.example.logintemplate.ui

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logintemplate.R
import com.example.logintemplate.databinding.ActivityEditBinding
import com.example.logintemplate.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class EditActivity:AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var auth: FirebaseAuth
    private var databaseReference :  DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private lateinit var imgUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")
        val user = auth.currentUser

        binding.profilePicture.setOnClickListener {
            changePicture()
        }

        binding.usernameEdittext.setOnClickListener {
            changeName()
        }

        binding.emailEdittext.setOnClickListener {
            changeEmail()
        }
        loadProfile()
        checkandGetpermissions()
    }

    private fun loadProfile(){

        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        binding.tvEmail.text = user?.email

        userreference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                binding.tvEmail.text = snapshot.child("email").value.toString()
                binding.tvUsername.text = snapshot.child("username").value.toString()
                //  binding?.textViewName?.text = snapshot.child("name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun checkandGetpermissions(){
        if(
            checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
        ){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
        }
        else{
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    fun changePicture(){
        Log.d("mssg", "button pressed")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 250)
    }

    fun changeName(){

        val user = auth.currentUser
        // creating custom dialog
        val dialog = Dialog(this@EditActivity)

        // setting content view to dialog
        dialog.setContentView(R.layout.edit_name_dialogue)

        // getting reference of TextView
        val dialogButtonYes = dialog.findViewById(R.id.save_button) as Button
        val dialogButtonNo = dialog.findViewById(R.id.cancel_button) as Button

        // click listener for No
        dialogButtonNo.setOnClickListener { // dismiss the dialog
            dialog.dismiss()
        }

        dialogButtonYes.setOnClickListener newUsername@{
            var newUsername = binding.usernameEdittext.text.toString()

            if (newUsername.isEmpty()){
                binding.usernameEdittext.error = "Email Harus Terisi"
                binding.usernameEdittext.requestFocus()
                return@newUsername
            }

            user?.let {
                user.updateEmail(newUsername).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Username Berhasil Diubah", Toast.LENGTH_SHORT).show()
                        // startActivity(Intent(this, UserFragment::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            dialog.dismiss()

        }

        // show the exit dialog
        dialog.show()

    }

    fun changeEmail(){

        val user = auth.currentUser
        // creating custom dialog
        val dialog = Dialog(this@EditActivity)

        // setting content view to dialog
        dialog.setContentView(R.layout.edit_email_dialogue)

        // getting reference of TextView
        val dialogButtonYes = dialog.findViewById(R.id.save_button) as Button
        val dialogButtonNo = dialog.findViewById(R.id.cancel_button) as Button

        // click listener for No
        dialogButtonNo.setOnClickListener { // dismiss the dialog
            dialog.dismiss()
        }


        // click listener for Yes
        dialogButtonYes.setOnClickListener newEmail@{ // dismiss the dialog and exit the exit
            var newEmail = binding.emailEdittext.text.toString()

            if (newEmail.isEmpty()){
                binding.emailEdittext.error = "Email Harus Terisi"
                binding.emailEdittext.requestFocus()
                return@newEmail
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                binding.emailEdittext.error = "Email Tidak Valid"
                binding.emailEdittext.requestFocus()
                return@newEmail
            }

            user?.let {
                user.updateEmail(newEmail).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Email Berhasil Diubah", Toast.LENGTH_SHORT).show()
                       // startActivity(Intent(this, UserFragment::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }


            dialog.dismiss()
        }

        // show the exit dialog
        dialog.show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 250){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadImgToFirebase(imgBitmap)
        }

    }

    private fun uploadImgToFirebase(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img_user/${FirebaseAuth.getInstance().currentUser?.email}")
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val img = baos.toByteArray()
        ref.putBytes(img)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener { Task->
                        Task.result.let{ Uri ->
                            imgUri = Uri
                            binding.profilePicture.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
    }

}