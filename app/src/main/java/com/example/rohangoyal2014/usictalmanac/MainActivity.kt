package com.example.rohangoyal2014.usictalmanac

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    var toolbar: Toolbar?=null
    var tabLayout:TabLayout?=null
    var viewPager:ViewPager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //toolbar=findViewById(R.id.toolbar)
        tabLayout=findViewById(R.id.tab_layout)
        viewPager=findViewById(R.id.pager)
        setUpToolbar()
        Utilities.FirebaseUtilites.mAuth= FirebaseAuth.getInstance()
    }
    private fun setUpToolbar(){

        viewPager?.adapter=MainActivityPagerAdapter(supportFragmentManager)
        tabLayout?.setupWithViewPager(viewPager)
        //setSupportActionBar(toolbar)
    }

    override fun onStart() {
        super.onStart()
        val user=Utilities.FirebaseUtilites.mAuth.currentUser
        manageUser(user)
    }

    private fun manageUser(user:FirebaseUser?){
        if(user!=null) {
            startActivity(Intent(this, EventsActivity::class.java))
            finish()
        }
    }
}
