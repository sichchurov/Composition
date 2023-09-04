package com.shchurovsi.composition.presentation

sealed class State

class FormatTime(val time: String) : State()

class PercentOfRightAnswers(val percent: Int) : State()

class ProgressAnswers(val answers: String) : State()