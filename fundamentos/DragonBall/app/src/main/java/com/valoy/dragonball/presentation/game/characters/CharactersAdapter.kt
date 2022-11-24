package com.valoy.dragonball.presentation.game.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.valoy.dragonball.R
import com.valoy.dragonball.databinding.CharacterItemBinding
import com.valoy.dragonball.dto.CharacterDTO

class CharactersAdapter(
    private val characters: List<CharacterDTO>,
    private val listener: Listener
) :
    RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CharacterItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.binding.powerText.text = character.power.toString()
        holder.binding.nameText.text = character.name
        holder.binding.descriptionText.text = character.description

        val drawable = if (character.power > 0) AppCompatResources.getDrawable(
            holder.binding.root.context,
            R.drawable.ic_thumb_up
        ) else AppCompatResources.getDrawable(holder.binding.root.context, R.drawable.ic_thumb_down)

        holder.binding.icon.setImageDrawable(drawable)

        holder.binding.root.setOnClickListener {
            listener.onClickListener(character)
        }
    }

    override fun getItemCount() = characters.size

    interface Listener {
        fun onClickListener(character: CharacterDTO)
    }
}
