package com.sh1p1lov.streamingmusicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sh1p1lov.streamingmusicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}