package com.shchurovsi.composition.domain.usecases

import com.shchurovsi.composition.domain.entity.GameSettings
import com.shchurovsi.composition.domain.entity.Level
import com.shchurovsi.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}