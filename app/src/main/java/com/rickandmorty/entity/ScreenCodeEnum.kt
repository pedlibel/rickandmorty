package com.rickandmorty.entity

import com.rickandmorty.presentation.common.base.BaseActivity
import com.rickandmorty.presentation.common.base.BaseFragment
import com.rickandmorty.presentation.features.character.details.CharacterDetailsActivity
import com.rickandmorty.presentation.features.character.details.main.CharacterDetailsMainFragment
import com.rickandmorty.presentation.features.character.list.CharacterListActivity
import com.rickandmorty.presentation.features.character.list.main.CharacterListMainFragment

const val SCREEN_PREFIX = "R&M"

enum class ScreenCodeEnum(val code: String) {
    SCREEN_UNKNOWN("${SCREEN_PREFIX}0000"),

    // Activities
    SCREEN_CHARACTER_LIST_ACTIVITY("${SCREEN_PREFIX}1000"),
    SCREEN_CHARACTER_DETAILS_ACTIVITY("${SCREEN_PREFIX}1001"),

    // Fragments
    SCREEN_CHARACTER_LIST_FRAGMENT("${SCREEN_PREFIX}2000"),
    SCREEN_CHARACTER_DETAILS_FRAGMENT("${SCREEN_PREFIX}2001"),
    ;

    companion object {
        fun screenToCode(activity: BaseActivity<*, *>) =
            when (activity) {
                is CharacterListActivity -> SCREEN_CHARACTER_LIST_ACTIVITY
                is CharacterDetailsActivity -> SCREEN_CHARACTER_DETAILS_ACTIVITY
                else -> SCREEN_UNKNOWN
            }

        fun screenToCode(fragment: BaseFragment<*, *>) =
            when (fragment) {
                is CharacterListMainFragment -> SCREEN_CHARACTER_LIST_FRAGMENT
                is CharacterDetailsMainFragment -> SCREEN_CHARACTER_DETAILS_FRAGMENT
                else -> SCREEN_UNKNOWN
            }
    }
}
