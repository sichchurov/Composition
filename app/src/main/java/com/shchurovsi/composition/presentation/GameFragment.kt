package com.shchurovsi.composition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.shchurovsi.composition.R
import com.shchurovsi.composition.databinding.FragmentGameBinding
import com.shchurovsi.composition.domain.entity.GameResult
import com.shchurovsi.composition.domain.entity.Level

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null

    private lateinit var level: Level

    private lateinit var result: GameResult

    private val viewModelFactory by lazy {
        GameViewModelFactory(requireActivity().application, level)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val optionViews: MutableList<TextView> by lazy {
        mutableListOf(
            binding.tvOption1,
            binding.tvOption2,
            binding.tvOption3,
            binding.tvOption4,
            binding.tvOption5,
            binding.tvOption6
        )
    }

    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showTimer()

        showProgress()

        solveQuestion()

        showQuestion()

        showProgressbar()

        finishGame()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.resetSettings()
    }

    private fun showTimer() {
        viewModel.apply {
            formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }
        }
    }

    private fun showProgress() {
        viewModel.apply {
            progressAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.text = it
            }
        }
    }

    private fun showQuestion() {
        viewModel.question.observe(viewLifecycleOwner) {
            binding.apply {
                tvSum.text = it.sum.toString()
                tvLeftNumber.text = it.visibleNumber.toString()
                for (index in optionViews.indices) {
                    optionViews[index].text = it.options[index].toString()
                }
            }
        }
    }

    private fun solveQuestion() {
        optionViews.forEach { v ->
            v.setOnClickListener {
                viewModel.chooseAnswer(v.text.toString().toInt())
            }
        }
    }

    private fun parseParams() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    private fun showProgressbar() {
        showRightAnswerProgress()
        showSecondaryProgress()
    }

    private fun showSecondaryProgress() {
        viewModel.minPercent.observe(viewLifecycleOwner) {
            binding.apply {
                progressBar.secondaryProgress = it
            }
        }
    }

    private fun showRightAnswerProgress() {
        binding.apply {

            viewModel.enoughRightAnswerPercent.observe(viewLifecycleOwner) {
                progressBar.progressTintList = ColorStateList.valueOf(getColorByState(it))
            }

            viewModel.enoughRightAnswerCounter.observe(viewLifecycleOwner) {
                tvAnswersProgress.setTextColor(getColorByState(it))
            }

            viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
                progressBar.setProgress(it, true)

            }
        }
    }

    private fun getColorByState(state: Boolean): Int {
        val color = if (state) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }

        return ContextCompat.getColor(requireContext(),color)
    }

    private fun finishGame() {
        viewModel.apply {

            gameResult.observe(viewLifecycleOwner) {
                result = it
            }


            shouldGameFinished.observe(viewLifecycleOwner) {
                if (it) {
                    requireActivity().supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        addToBackStack(null)
                        replace(R.id.main_container, FinishGameFragment.newInstance(result))
                    }
                }
            }


        }
    }

    companion object {
        private const val KEY_LEVEL = "level"
        const val NAME = "GameFragment"

        @JvmStatic
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}