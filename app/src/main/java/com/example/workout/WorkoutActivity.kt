package com.example.workout

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class WorkoutActivity : AppCompatActivity() {

    private val viewModel: WorkoutViewModel by viewModels()

    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var imageView: ImageView
    private lateinit var timerText: TextView
    private lateinit var startButton: Button

    private lateinit var startSound: MediaPlayer
    private lateinit var pauseSound: MediaPlayer
    private lateinit var finishSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val workoutId = intent.getIntExtra("WORKOUT_ID", 1)
        viewModel.loadWorkout(workoutId)

        titleText = findViewById(R.id.textTitle)
        descriptionText = findViewById(R.id.textDescription)
        imageView = findViewById(R.id.imageView)
        timerText = findViewById(R.id.timerText)
        startButton = findViewById(R.id.startButton)

        startSound = MediaPlayer.create(this, R.raw.start_sound)
        pauseSound = MediaPlayer.create(this, R.raw.timer_pause)
        finishSound = MediaPlayer.create(this, R.raw.workout_beendet)

        viewModel.exercise.observe(this, Observer { exercise ->
            titleText.text = exercise.title
            descriptionText.text = exercise.description
            imageView.setImageResource(exercise.imageRes)
        })

        viewModel.timerText.observe(this, Observer {
            timerText.text = it
        })

        viewModel.signal.observe(this, Observer { signal ->
            when (signal) {
                "START" -> startSound.start()
                "PAUSE" -> pauseSound.start()
                "END" -> finishSound.start()
            }
        })

        viewModel.startable.observe(this, Observer { canStart ->
            startButton.isEnabled = canStart
        })

        viewModel.finished.observe(this, Observer { finished ->
            if (finished) {
                saveProgress(workoutId)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("FINISHED", true)
                startActivity(intent)
                finish()
            }
        })

        startButton.setOnClickListener {
            startButton.isEnabled = false
            viewModel.startCurrentExercise()
        }
    }

    private fun saveProgress(workoutId: Int) {
        val prefs = getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
        val completed = prefs.getInt("completedWorkouts", 0)
        val alreadyDone = prefs.getBoolean("workout_done_$workoutId", false)
        with(prefs.edit()) {
            if (!alreadyDone) putInt("completedWorkouts", completed + 1)
            putBoolean("workout_done_$workoutId", true)
            apply()
        }
    }
}
