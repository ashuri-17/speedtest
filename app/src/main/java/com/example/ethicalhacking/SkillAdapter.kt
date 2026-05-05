package com.example.ethicalhacking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SkillAdapter(
    private val context: Context,
    private val skills: List<Skill>,
    private val onLevelChanged: () -> Unit
) : RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_skill, parent, false)
        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val skill = skills[position]
        holder.name.text = skill.name
        // Set up spinner
        val levels = (1..5).map { it.toString() }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, levels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.levelSpinner.adapter = adapter
        holder.levelSpinner.setSelection(skill.level - 1)
        holder.levelSpinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, pos: Int, id: Long) {
                skill.level = pos + 1
                onLevelChanged()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })
    }

    override fun getItemCount(): Int = skills.size

    class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvSkillName)
        val levelSpinner: Spinner = itemView.findViewById(R.id.spinnerLevel)
    }
}
