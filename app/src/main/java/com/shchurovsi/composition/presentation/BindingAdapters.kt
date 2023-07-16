package com.shchurovsi.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.shchurovsi.composition.R
import com.shchurovsi.composition.domain.entity.GameResult

interface OptionClickListener {
    fun optionClick(opt: Int)
}

@BindingAdapter("requireAnswers")
fun bindRequireAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("rightAnswers")
fun bindRightAnswers(textView: TextView, score: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score_answers),
        score
    )
}

@BindingAdapter("minPercentOfRightAnswers")
fun bindMinPercentOfRightAnswers(textView: TextView, percent: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        percent
    )
}

@BindingAdapter("percentOfRightAnswers")
fun bindPercentOfRightAnswers(textView: TextView, result: GameResult) {
    val percent = ((result.countOfRightAnswers / result.countOfQuestions.toDouble()) * 100).toInt()
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        percent
    )
}

@BindingAdapter("winnerEmoji")
fun setWinnerEmoji(imageView: ImageView, isWinner: Boolean) {
    if (isWinner) {
        imageView.setImageResource(R.drawable.ic_smile)
    } else {
        imageView.setImageResource((R.drawable.ic_sad))
    }
}

@BindingAdapter("progressEnoughRightAnswerPercent")
fun bindEnoughRightAnswerPercent(progressBar: ProgressBar, state: Boolean) {
    val ctx = progressBar.context
    progressBar.progressTintList = ColorStateList.valueOf(getColorByState(ctx, state))
}

@BindingAdapter("progressEnoughRightAnswerCounter")
fun bindEnoughRightAnswerCounter(textView: TextView, state: Boolean) {
    val ctx = textView.context
    textView.setTextColor(getColorByState(ctx, state))
}

@BindingAdapter("percentOfRightAnswers")
fun bindPercentOfRightAnswers(progressBar: ProgressBar, percent: Int) {
    progressBar.setProgress(percent, true)
}

private fun getColorByState(ctx: Context, state: Boolean): Int {
    val color = if (state) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }

    return ContextCompat.getColor(ctx, color)
}

@BindingAdapter("tvNumberAsText")
fun bindTvNumber(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onClickListenerOption")
fun bindOnClickListenerOption(textView: TextView, clickListener: OptionClickListener) {
    textView.setOnClickListener {
        clickListener.optionClick(textView.text.toString().toInt())
    }
}