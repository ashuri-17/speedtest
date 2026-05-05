package com.freeclaudecode.speedtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.freeclaudecode.speedtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.btnStartTest.setOnClickListener {
            startSpeedTest()
        }
    }

    private fun startSpeedTest() {
        Toast.makeText(this, "Speed test starting...", Toast.LENGTH_SHORT).show()
        SpeedTestManager(this).runTest(binding)
    }
}
