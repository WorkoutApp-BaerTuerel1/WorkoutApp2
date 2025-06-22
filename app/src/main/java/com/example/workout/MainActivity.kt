package com.example.workout

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    private val workoutButtons by lazy {
        listOf(
            findViewById<Button>(R.id.buttonWorkout1),
            findViewById<Button>(R.id.buttonWorkout2),
            findViewById<Button>(R.id.buttonWorkout3)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)

        val sharedPrefs = getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
        val completed = sharedPrefs.getInt("completedWorkouts", 0)

        progressBar.max = 3
        progressBar.progress = completed
        progressText.text = "$completed / 3 Workouts abgeschlossen"

        for ((index, button) in workoutButtons.withIndex()) {
            val workoutId = index + 1
            val done = sharedPrefs.getBoolean("workout_done_$workoutId", false)
            if (done) button.setBackgroundColor(Color.GREEN)

            button.setOnClickListener {
                val intent = Intent(this, WorkoutActivity::class.java)
                intent.putExtra("WORKOUT_ID", workoutId)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPrefs = getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
        val completed = sharedPrefs.getInt("completedWorkouts", 0)
        progressBar.progress = completed
        progressText.text = "$completed / 3 Workouts abgeschlossen"

        for ((index, button) in workoutButtons.withIndex()) {
            val workoutId = index + 1
            val done = sharedPrefs.getBoolean("workout_done_$workoutId", false)
            if (done) button.setBackgroundColor(Color.GREEN)
        }
    }
}

