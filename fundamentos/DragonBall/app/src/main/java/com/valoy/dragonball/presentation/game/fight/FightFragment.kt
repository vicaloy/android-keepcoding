package com.valoy.dragonball.presentation.game.fight

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.valoy.dragonball.R
import com.valoy.dragonball.databinding.FragmentFightBinding
import com.valoy.dragonball.dto.CharacterDTO
import com.valoy.dragonball.presentation.game.characters.CharactersFragment

class FightFragment : Fragment() {

    private val viewModel by viewModels<FightViewModel> {
        FightViewModelFactory()
    }
    private var binding: FragmentFightBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFightBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedId = arguments?.getString(CharactersFragment.SELECTED_ID) ?: ""
        val randomId = arguments?.getString(CharactersFragment.RANDOM_ID) ?: ""

        viewModel.onShowCharacters(selectedId, randomId)
        bindFight()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun observeState() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { state ->
                when (state) {
                    is FightViewModel.State.Winner -> {
                        showWinner(state.name)
                    }
                    is FightViewModel.State.Characters -> {
                        bindCharacters(state.selected, state.random)
                    }
                }
            }
        }
    }

    private fun showWinner(winnerName: String) {
        AlertDialog.Builder(context)
            .setTitle(resources.getString(R.string.winner))
            .setMessage(winnerName)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->

                findNavController().navigate(R.id.action_navigation_fight_to_navigation_characters)
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
            .show()
    }

    private fun bindCharacters(selected: CharacterDTO, random: CharacterDTO) {
        binding?.selectedText?.text = selected.name
        binding?.selectedProgress?.progress = selected.power

        binding?.randomText?.text = random.name
        binding?.randomProgress?.progress = random.power
    }

    private fun bindFight() {
        binding?.fightButton?.setOnClickListener {
            val selectedId = arguments?.getString(CharactersFragment.SELECTED_ID) ?: ""
            val randomId = arguments?.getString(CharactersFragment.RANDOM_ID) ?: ""

            viewModel.onFightCharacters(selectedId, randomId)
        }
    }

}