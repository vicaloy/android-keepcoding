package com.valoy.dragonball.presentation.game.characters

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.valoy.dragonball.R
import com.valoy.dragonball.databinding.FragmentCharactersBinding
import com.valoy.dragonball.dto.CharacterDTO


class CharactersFragment : Fragment(), CharactersAdapter.Listener {

    private val viewModel by viewModels<CharactersViewModel> { CharactersViewModelFactory() }

    private var binding: FragmentCharactersBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
    }

    private fun observeState() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is CharactersViewModel.State.Loading -> {
                    binding?.progress?.visibility = View.VISIBLE
                }
                is CharactersViewModel.State.Success -> {
                    bindRecycler(it.characters)
                    it.winner?.let { winner -> showWinner(winner) }
                    binding?.progress?.visibility = View.GONE
                }
                is CharactersViewModel.State.Error -> {
                    binding?.progress?.visibility = View.GONE
                }
                is CharactersViewModel.State.Fight -> {
                    fightNavigate(it.selectedId, it.randomId)
                }
            }
        }
    }

    private fun bindRecycler(characters: List<CharacterDTO>) {
        val adapter = CharactersAdapter(characters, this)
        binding?.recycler?.adapter = adapter
        binding?.recycler?.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding?.recycler?.context,
            (binding?.recycler?.layoutManager as LinearLayoutManager).orientation
        )
        binding?.recycler?.addItemDecoration(dividerItemDecoration)
    }

    private fun fightNavigate(selected: String, random: String) {
        val bundle = Bundle()
        bundle.putString(SELECTED_ID, selected)
        bundle.putString(RANDOM_ID, random)

        findNavController().navigate(R.id.action_navigation_characters_to_navigation_fight, bundle)
    }

    private fun showWinner(winnerName: String){
        AlertDialog.Builder(context)
            .setTitle(resources.getString(R.string.winner))
            .setMessage(winnerName)
            .setPositiveButton(android.R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onClickListener(character: CharacterDTO) {
        viewModel.onCharacterClick(character)
    }

    companion object{
        const val SELECTED_ID = "selected_id"
        const val RANDOM_ID = "random_id"
    }
}