package com.rickandmorty.presentation.features.character.list.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rickandmorty.databinding.CharacterItemBinding
import com.rickandmorty.entity.Character
import com.rickandmorty.presentation.common.extension.basicDiffUtil

class CharacterListAdapter(
    private val clickAction: ((Character) -> Unit),
) : RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {

    var characterList: List<Character> by basicDiffUtil(
        emptyList(),
    )

    inner class CharacterViewHolder(val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding =
            CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.binding.apply {
            characterList.getOrNull(position)?.let { character ->
                setDataBinding(character)
                setListeners(character)
            }
        }
    }

    fun CharacterItemBinding.setDataBinding(character: Character) {
        characterBinding = character
    }

    fun CharacterItemBinding.setListeners(character: Character) {
        root.setOnClickListener {
            clickAction(character)
        }
    }

    override fun getItemCount(): Int {
        return characterList.size
    }
}
