package com.shchurovsi.composition.data

import com.shchurovsi.composition.domain.entity.GameSettings
import com.shchurovsi.composition.domain.entity.Level
import com.shchurovsi.composition.domain.entity.Question
import com.shchurovsi.composition.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(rightAnswer - countOfOptions, MIN_SUM_VALUE)
        val to = min(rightAnswer + countOfOptions, maxSumValue)
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> {
                GameSettings(
                    10,
                    2,
                    50,
                    8
                )
            }

            Level.EASY -> {
                GameSettings(
                    20,
                    4,
                    60,
                    50
                )
            }

            Level.NORMAL -> {
                GameSettings(
                    30,
                    3,
                    70,
                    45
                )
            }

            Level.HARD -> {
                GameSettings(
                    30,
                    2,
                    85,
                    35
                )
            }
        }
    }
}