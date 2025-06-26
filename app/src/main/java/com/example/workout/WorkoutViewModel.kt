package com.example.workout

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkoutViewModel : ViewModel() {

    // === 1. Alle Workouts ===
    private val workouts = listOf(
        // Workout 1: Bauch
        listOf(
            Exercise("mountain climbers", "Gehe für die Mountain Climbers in die Liegestütz Position. Ziehe dann jeweils ein Knie abwechselnd zur Brust. Achte dabei darauf das dein Rumpf stabil bleibt.", R.drawable.mountain),
            Exercise("Plank", "Gehe wieder in die Liegestütz Position, nur dass du dich jetzt nicht auf den Händen, sondern den Ellbogen abstützt. Nun halte diese Position.", R.drawable.plank),
            Exercise("sit ups", "Lege dich auf den Rücken, winkele die Beine in einem 90 Grad Winkel an und verschränke die Arme vor dem Körper. Hebe nun deinen Oberkörper an.", R.drawable.sit),
            Exercise("Leg Raises", "Lege dich flach auf den Rücken mit den Armen parallel zum Körper. Hebe nun deine Beine an. Stelle sicher, dass deine Beine gerade bleiben und spanne deinen Bauch an.", R.drawable.leg),
            Exercise("Plank", "Gehe wieder in die Liegestütz Position, nur dass du dich jetzt nicht auf den Händen, sondern den Ellbogen abstützt. Nun halte diese Position.", R.drawable.plank)
        ),
        // Workout 2: Beine
        listOf(
            Exercise("Squads", "Stelle dich aufrecht hin mit den Füßen parallel auf Schulterhöhe. Gehe nun in die Hocke, bis deine Beine einen 90 Grad Winkel erreicht haben und gehe dann wieder nach oben.", R.drawable.kniebeuge),
            Exercise("Skippings", "Stelle dich aufrecht hin. Ziehe nun jeweils ein Knie abwechselnd nach oben. Nimm hierbei die Arme mit.", R.drawable.skippings),
            Exercise("Ausfallschritte", "Stelle dich aufrecht hin. Gehe nun mit einem Bein nach vorne und lasse deine Hüfte ab, bis in beiden Beinen ein 90 Grad Winkel vorhanden ist. Deine Füße sollten immer parallel stehen.", R.drawable.ausfall),
            Exercise("Donkey kicks", "Gehe in den Vierfüßlerstand. Kicke nun immer ein Bein nach hinten bis auf Höhe deines Rückens. Mache die Bewegung nicht zu schnell, sondern langsam und kontrolliert.", R.drawable.donkey),
            Exercise("Skippings", "Stelle dich aufrecht hin. Ziehe nun jeweils ein Knie abwechselnd nach oben. Nimm hierbei die Arme mit.", R.drawable.skippings)
        ),
        // Workout 3: Full Body
        listOf(
            Exercise("Jumping Jacks", "Stelle dich aufrecht hin. Springe nun in die Grätsche und führe deine Arme gleichzeitig über dem Kopf zusammen. Springe im nächsten Schritt wieder zurück in die Grundstellung.", R.drawable.hampel),
            Exercise("Push-Ups", "Stütze dich auf Händen und Füßen ab, sodass der Körper eine gerade Linie bildet. Gehe mit dem Oberkörper soweit hinunter, dass die Brust kurz vorm Boden ist, und drücke dich wieder hoch.", R.drawable.liege),
            Exercise("Bridge", "Lege dich auf den Boden und winkele deine Beine an. Hebe nun deine Hüfte an, bis dein Rücken und deine Beine eine Brücke bilden, und lasse dich dann wieder ab.", R.drawable.bruck),
            Exercise("Pull ups", "Hänge dich mit einem schulterbreiten Griff an eine Stange. Ziehe nun deinen Körper nach oben, bis dein Kinn über der Stange ist, und lasse dich dann wieder ab.", R.drawable.klimmzug),
            Exercise("Jumping Jacks", "Stelle dich aufrecht hin. Springe nun in die Grätsche und führe deine Arme gleichzeitig über dem Kopf zusammen. Springe im nächsten Schritt wieder zurück in die Grundstellung.", R.drawable.hampel)
        )
    )

    // === 2. LiveData
    private val _exercise = MutableLiveData<Exercise>()
    val exercise: LiveData<Exercise> = _exercise

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    private val _signal = MutableLiveData<String>()
    val signal: LiveData<String> = _signal

    private val _finished = MutableLiveData<Boolean>()
    val finished: LiveData<Boolean> = _finished

    private val _startable = MutableLiveData<Boolean>()
    val startable: LiveData<Boolean> = _startable

    // === 3. Zustand
    private var currentWorkout: List<Exercise> = emptyList()
    private var currentIndex = 0

    // === 4. Workout laden
    fun loadWorkout(id: Int) {
        currentWorkout = workouts[id - 1]
        currentIndex = 0
        _exercise.value = currentWorkout[currentIndex]
        _startable.value = true
        _timerText.value = "Bereit?"
    }

    // === 5. Übung starten
    fun startCurrentExercise() {
        _signal.value = "START"
        _startable.value = false

        startTimer(45_000) {
            currentIndex++
            if (currentIndex < currentWorkout.size) {
                _exercise.value = currentWorkout[currentIndex]
                beginPausePhase()
            } else {
                _signal.value = "END"
                _finished.value = true
            }
        }
    }

    // === 6. Pause starten (nur wenn Workout noch läuft)
    private fun beginPausePhase() {
        _signal.value = "PAUSE"
        _timerText.value = "Pause…"

        startTimer(15_000) {
            _startable.value = true
            _timerText.value = "Bereit für Start"
        }
    }

    // === 7. Timer
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


