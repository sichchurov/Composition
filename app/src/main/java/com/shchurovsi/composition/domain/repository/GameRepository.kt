package com.shchurovsi.composition.domain.repository

import com.shchurovsi.composition.domain.entity.GameSettings
import com.shchurovsi.composition.domain.entity.Level
import com.shchurovsi.composition.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}