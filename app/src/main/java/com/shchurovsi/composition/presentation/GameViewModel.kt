package com.shchurovsi.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shchurovsi.composition.R
import com.shchurovsi.composition.data.GameRepositoryImpl
import com.shchurovsi.composition.domain.entity.GameResult
import com.shchurovsi.composition.domain.entity.GameSettings
import com.shchurovsi.composition.domain.entity.Level
import com.shchurovsi.composition.domain.entity.Question
import com.shchurovsi.composition.domain.usecases.GenerateQuestionUseCase
import com.shchurovsi.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private val repository = GameRepositoryImpl

    private lateinit var gameSettings: GameSettings

    private var timer: CountDownTimer? = null

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var rightAnswersCounter = 0
    private var answersCounter = 0

    // timer
    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    // questions initializing
    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    // progress string
    private val _percentOfRightAnswers = MutableLiveData(0)
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughRightAnswerCounter = MutableLiveData<Boolean>()
    val enoughRightAnswerCounter: LiveData<Boolean>
        get() = _enoughRightAnswerCounter

    private val _enoughRightAnswerPercent = MutableLiveData<Boolean>()
    val enoughRightAnswerPercent: LiveData<Boolean>
        get() = _enoughRightAnswerPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    init {
        startGame()
    }

    private fun startGame() {
        generateSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }

    private fun generateSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECOND,
            MILLIS_IN_SECOND
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }.start()
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun updateProgress() {
        getProgress()

        val percent = calculateRightAnswers()
        _percentOfRightAnswers.value = percent
        _enoughRightAnswerCounter.value = rightAnswersCounter >= gameSettings.minCountOfRightAnswers
        _enoughRightAnswerPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun getProgress() {
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers),
            rightAnswersCounter,
            gameSettings.minCountOfRightAnswers
        )
    }

    private fun calculateRightAnswers(): Int {
        if (answersCounter == 0) {
            return 0
        }
        return ((rightAnswersCounter / answersCounter.toDouble()) * 100).toInt()
    }

    fun chooseAnswer(hiddenNumber: Int) {
        checkAnswers(hiddenNumber)
        updateProgress()
        generateQuestion()
    }

    private fun checkAnswers(hiddenNumber: Int) {
        if (hiddenNumber == question.value?.rightAnswer) {
            rightAnswersCounter++
        }
        answersCounter++
    }

    // format time to 00:59
    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECOND
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughRightAnswerCounter.value == true && enoughRightAnswerPercent.value == true,
            rightAnswersCounter,
            answersCounter,
            gameSettings
        )
    }

    companion object {
        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}