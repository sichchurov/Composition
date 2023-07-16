package com.shchurovsi.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shchurovsi.composition.databinding.FragmentFinishGameBinding

class FinishGameFragment : Fragment() {

    private val args by navArgs<FinishGameFragmentArgs>()

    private var _binding: FragmentFinishGameBinding? = null
    private val binding: FragmentFinishGameBinding
        get() = _binding ?: throw RuntimeException("FragmentFinishGameBinding is null")

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

        binding.apply {
            buttonRetry.setOnClickListener { retryGame() }
            gameResult = args.gameResult
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }
}