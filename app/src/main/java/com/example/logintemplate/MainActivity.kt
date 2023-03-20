package com.example.logintemplate

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.logintemplate.adapter.ItemClickCallBack
import com.example.logintemplate.adapter.MainAdapter
import com.example.logintemplate.databinding.ActivityHomeBinding
import com.example.logintemplate.databinding.ActivityMainBinding
import com.example.logintemplate.databinding.ActivitySignupBinding
import com.example.logintemplate.model.MainItem
import com.example.logintemplate.ui.DetailActivity
import com.example.logintemplate.ui.ProfileActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    lateinit var auth: FirebaseAuth
    private lateinit var mainAdapter: MainAdapter
    private lateinit var rvItem: RecyclerView
    private val list = ArrayList<MainItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvItem = findViewById(R.id.rv_item)
        rvItem.setHasFixedSize(true)
        list.addAll(getListHeroes())
        showRecyclerList()
//        setUpRecyclerView()

    }

    @SuppressLint("Recycle")
    private fun getListHeroes(): ArrayList<MainItem> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listHero = ArrayList<MainItem>()
        for (i in dataName.indices) {
            val hero = MainItem(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listHero.add(hero)
        }
        return listHero
    }

    private fun showRecyclerList() {
        rvItem.layoutManager = LinearLayoutManager(this, )
        val listHeroAdapter = MainAdapter(list)
        rvItem.adapter = listHeroAdapter
        listHeroAdapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MainItem) {
                showSelectedItem(data)
            }
        })
    }


    //bila meng-click user
    private fun showSelectedItem(user: MainItem) {
        Toast.makeText(this, "You Choose " + user.nama, Toast.LENGTH_SHORT).show()

        val moveWithObjectIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_ITEM, user)

        startActivity(moveWithObjectIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //memilih menu di option bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }

            else -> false
        }
    }

}