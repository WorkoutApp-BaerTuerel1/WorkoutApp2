package com.example.workout

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkoutViewModel : ViewModel() {

    private val workouts = listOf(
        listOf(
            Exercise("Jumping Jacks", "Aufwärmen mit Hampelmännern", R.drawable.jumping_jacks),
            Exercise("Push-Ups", "Liegestütze für Brust und Arme", R.drawable.pushups),
            Exercise("Squats", "Kniebeugen für die Beine", R.drawable.squats),
            Exercise("Plank", "Unterarmstütz für den Core", R.drawable.plank),
            Exercise("Lunges", "Ausfallschritte für Beine und Po", R.drawable.lunges)
        ),
        listOf(/* Workout 2 */),
        listOf(/* Workout 3 */)
    )

    private val _exercise = MutableLiveData<Exercise>()
    val exercise: LiveData<Exercise> = _exercise

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    private val _signal = MutableLiveData<String>()
    val signal: LiveData<String> = _signal

    private val _finished = MutableLiveData<Boolean>()
    val finished: LiveData<Boolean> = _finished

    private var currentWorkout: List<Exercise> = emptyList()
    private var currentIndex = 0

    fun loadWorkout(id: Int) {
        currentWorkout = workouts[id - 1]
        currentIndex = 0
        _exercise.value = currentWorkout[currentIndex]
    }

    fun startCurrentExercise() {
        _signal.value = "START"
        startTimer(45_000) {
            _signal.value = "PAUSE"
            startPause()
        }
    }

    private fun startPause() {
        _signal.value = "PAUSE"
        startTimer(15_000) {
            currentIndex++
            if (currentIndex < currentWorkout.size) {
                _exercise.value = currentWorkout[currentIndex]
            } else {
                _signal.value = "END"
                _finished.value = true
            }
        }
    }

    private fun startTimer(millis: Long, onFinish: () -> Unit) {
        object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerText.value = "${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                onFinish()
            }
        }.start()
    }
}

// Exercise.kt
data class Exercise(val title: String, val description: String, val imageRes: Int)