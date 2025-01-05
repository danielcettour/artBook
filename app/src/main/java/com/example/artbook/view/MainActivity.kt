package com.example.artbook.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.artbook.R

class MainActivity : AppCompatActivity() {

    // @Inject
    // lateinit var fragmentFactory: ArtFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(R.layout.activity_main)
    }
}
