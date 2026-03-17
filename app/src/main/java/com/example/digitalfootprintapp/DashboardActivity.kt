package com.example.digitalfootprintapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val riskScore = findViewById<TextView>(R.id.riskScore)
        val riskLevel = findViewById<TextView>(R.id.riskLevel)
        val recommendBtn = findViewById<Button>(R.id.recommendBtn)

        val score = 35
        riskScore.text = score.toString()

        if (score <= 20) {
            riskLevel.text = "Low Risk"
            riskLevel.setTextColor(Color.GREEN)
        } else if (score <= 50) {
            riskLevel.text = "Medium Risk"
            riskLevel.setTextColor(Color.parseColor("#FFA000"))
        } else {
            riskLevel.text = "High Risk"
            riskLevel.setTextColor(Color.RED)
        }

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(7f, "Location"))
        entries.add(PieEntry(10f, "Camera"))
        entries.add(PieEntry(3f, "Microphone"))

        val dataSet = PieDataSet(entries, "Permissions")

        dataSet.colors = listOf(
            Color.parseColor("#42A5F5"),
            Color.parseColor("#EF5350"),
            Color.parseColor("#66BB6A"),
            Color.parseColor("#FFA726")
        )

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.centerText = "Permission Risk"
        pieChart.animateY(1000)
        pieChart.invalidate()

        recommendBtn.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }
    }
}