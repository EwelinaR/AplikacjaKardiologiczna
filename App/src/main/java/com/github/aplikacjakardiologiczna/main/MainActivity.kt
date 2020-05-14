package com.github.aplikacjakardiologiczna.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.aplikacjakardiologiczna.R

class MainActivity : AppCompatActivity(), MainContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
