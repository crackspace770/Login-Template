package com.example.logintemplate.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logintemplate.R
import com.example.logintemplate.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var auth: FirebaseAuth
    private var databaseReference :  DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = database?.reference!!.child("profile")

        if (uid.isNotEmpty()){

            loadProfile()
        }

    }

    private fun loadProfile(){

        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        binding.tvEmail.text = user?.email
        binding.tvUsername.text = user?.displayName

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

        binding.btnLogout.setOnClickListener {
            logout()
        }

        checkandGetpermissions()

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

    private fun logout(){
        auth.signOut()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
       // showLoading(true)
        startActivity(intent)
        onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }



    //memilih menu di option bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, EditActivity::class.java)
                startActivity(intent)
                true
            }

            else -> false
        }
    }

 //   private fun showLoading(isLoading: Boolean) {
 //       binding.apply {
 //           progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
 //       }
//    }
}