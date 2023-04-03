package com.rickandmorty.presentation.features.character.list.main

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.rickandmorty.R
import com.rickandmorty.databinding.CharacterListMainFragmentBinding
import com.rickandmorty.entity.Character
import com.rickandmorty.presentation.common.CHARACTER_ID
import com.rickandmorty.presentation.common.base.BaseFragment
import com.rickandmorty.presentation.features.character.details.CharacterDetailsActivity

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CharacterListMainFragment :
    BaseFragment<CharacterListMainFragmentBinding, CharacterListMainViewModel>(
        R.layout.character_list_main_fragment,
        CharacterListMainViewModel::class.java
    ) {

    private var characterDetailsActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.extras?.getInt(CHARACTER_ID, -1)
                    ?.let { characterId ->
                        if(characterId != -1)
                        viewModel.reloadCharacter(characterId)
                    }
            }
        }

    val adapter: CharacterListAdapter by lazy {
        createCharacterListAdapter()
    }

    override fun bindView() {
        super.bindView()
        binding.vm = viewModel
        initObservers()
    }

    fun initObservers() {
        viewModel.ldCharacterList.observe(viewLifecycleOwner) { characterList ->
            adapter.characterList = characterList
        }
    }

    override fun updateToolbar() {
        setToolbarTitle(R.string.character_list_title)
    }

    fun onItemClickAction(character: Character) {
        with(
            viewModel.getNavigationToCharacterDetails(character)
        ) {
            characterDetailsActivity.launch(
                Intent(
                    requireContext(),
                    CharacterDetailsActivity::class.java
                ).apply {
                    putExtras(arguments)
                }
            )
        }
    }

    fun createCharacterListAdapter() =
        CharacterListAdapter(::onItemClickAction).also {
            binding.rvCharacters.adapter = it
            binding.rvCharacters.layoutManager = LinearLayoutManager(context, VERTICAL, false)
            binding.rvCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastFinding =
                        (binding.rvCharacters.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                    if (lastFinding >= it.itemCount - viewModel.paginationRefresh &&
                        !viewModel.refreshing
                    ) {
                        requestSearch()
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }

    fun requestSearch() {
        viewModel.getCharactersNextPage()
    }
}
