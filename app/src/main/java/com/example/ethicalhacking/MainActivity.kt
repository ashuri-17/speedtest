package com.example.ethicalhacking

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvSkills: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView

    // Sample skill list
    private val skillList = mutableListOf(
        Skill("Network Scanning"),
        Skill("Vulnerability Assessment"),
        Skill("Web Exploitation"),
        Skill("Reverse Engineering"),
        Skill("Social Engineering")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvSkills = findViewById(R.id.rvSkills)
        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tvProgress)

        rvSkills.layoutManager = LinearLayoutManager(this)
        val adapter = SkillAdapter(this, skillList) { updateProgress() }
        rvSkills.adapter = adapter

        updateProgress()
    }

    private fun updateProgress() {
        // Count skills that have reached level 3 or higher (arbitrary target for white‑hat)
        val achieved = skillList.count { it.level >= 3 }
        progressBar.max = skillList.size
        progressBar.progress = achieved
        tvProgress.text = "Progress: $achieved/${skillList.size} skills at Level 3"
    }
}
