package com.shchurovsi.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.shchurovsi.composition.databinding.FragmentFinishGameBinding
import com.shchurovsi.composition.domain.entity.GameResult

class FinishGameFragment : Fragment() {

    private lateinit var result: GameResult

    private var _binding: FragmentFinishGameBinding? = null
    private val binding: FragmentFinishGameBinding
        get() = _binding ?: throw RuntimeException("FragmentFinishGameBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun parseParams() {
        requireArguments().getParcelable<GameResult>(GAME_STAT)?.let {
            result = it
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        private const val GAME_STAT = "stat"

        @JvmStatic
        fun newInstance(result: GameResult): FinishGameFragment {
            return FinishGameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_STAT, result)
                }
            }
        }
    }
}