package com.shchurovsi.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.shchurovsi.composition.R
import com.shchurovsi.composition.databinding.FragmentChooseLevelBinding
import com.shchurovsi.composition.domain.entity.Level


class ChooseLevelFragment : Fragment(), OnClickListener {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchGameFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFragment() = with(binding) {
        buttonLevelEasy.setOnClickListener(this@ChooseLevelFragment)
        buttonLevelNormal.setOnClickListener(this@ChooseLevelFragment)
        buttonLevelHard.setOnClickListener(this@ChooseLevelFragment)
        buttonLevelTest.setOnClickListener(this@ChooseLevelFragment)
    }

    override fun onClick(view: View?) {
        val fragment = when (view?.id) {
            R.id.button_level_easy -> GameFragment.newInstance(Level.EASY)
            R.id.button_level_test -> GameFragment.newInstance(Level.TEST)
            R.id.button_level_normal -> GameFragment.newInstance(Level.NORMAL)
            R.id.button_level_hard -> GameFragment.newInstance(Level.HARD)
            else -> throw RuntimeException("Fragment is undefined")
        }

        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(GameFragment.NAME)
            replace(R.id.main_container, fragment)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ChooseLevelFragment()
    }



}