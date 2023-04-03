@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.rickandmorty.presentation.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rickandmorty.presentation.common.extension.getScreenCode
import com.rickandmorty.presentation.common.extension.titleToCodeTitle
import org.koin.android.compat.ViewModelCompat

abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel>(
    private val layoutResId: Int,
    private val viewModelClass: Class<VM>
) : AppCompatActivity() {

    protected lateinit var binding: DB
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this
        initViewModel(savedInstanceState)
        bindView()
        setUpToolbar()
    }

    abstract fun setUpToolbar()

    private fun initViewModel(savedInstanceState: Bundle?) {
        viewModel = ViewModelCompat.getViewModel(this, viewModelClass)
        viewModel.initViewModel(intent.extras, savedInstanceState)
    }

    fun setToolbarTitle(title: Int) {
        supportActionBar?.title = getScreenCode().titleToCodeTitle(getString(title))
    }

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = getScreenCode().titleToCodeTitle(title)
    }

    open fun bindView() {}
}
