package com.example.rohangoyal2014.usictalmanac

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar

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
    }
    private fun setUpToolbar(){

        viewPager?.adapter=MainActivityPagerAdapter(supportFragmentManager)
        tabLayout?.setupWithViewPager(viewPager)
        //setSupportActionBar(toolbar)
    }
}
