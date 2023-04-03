@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.rickandmorty.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.rickandmorty.R
import com.rickandmorty.entity.ServiceException
import com.rickandmorty.presentation.common.extension.getScreenCode
import com.rickandmorty.presentation.common.extension.ignoreNull
import com.rickandmorty.presentation.common.extension.observeEvent
import com.rickandmorty.presentation.common.extension.titleToCodeTitle
import org.koin.android.compat.ViewModelCompat.getViewModel

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel>(
    private val layoutResId: Int,
    private val viewModelClass: Class<VM>,
) : Fragment() {

    protected lateinit var binding: DB
    protected lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = this
        initViewModel(savedInstanceState)
        bindView()
        updateToolbar()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreated(savedInstanceState)
    }

    fun setToolbarTitle(titleId: Int) {
        requireActivityAsBaseActivity()?.supportActionBar?.title =
            getScreenCode().titleToCodeTitle(getString(titleId))
    }

    fun setToolbarTitle(titleString: String) {
        requireActivityAsBaseActivity()?.supportActionBar?.title =
            getScreenCode().titleToCodeTitle(titleString)
    }

    private fun initViewModel(savedInstanceState: Bundle?) {
        viewModel = getViewModel(this, viewModelClass)
        viewModel.initViewModel(addActivityArgument(), savedInstanceState)
    }

    protected fun requireActivityAsBaseActivity() =
        requireActivity() as? BaseActivity<*, *>

    private fun addActivityArgument(): Bundle? {
        return ignoreNull(activity?.intent?.extras, arguments) { notNullArguments ->
            Bundle().apply {
                notNullArguments.forEach { argument ->
                    putAll(argument)
                }
            }
        }
    }

    private fun initBaseObserver() {
        viewModel.ldServiceExceptionManager.observeEvent(viewLifecycleOwner) { serviceException ->
            manageServiceException(serviceException)
        }

        viewModel.ldExceptionManager.observeEvent(viewLifecycleOwner) { throwable ->
            manageException(throwable)
        }
    }

    private fun getErrorText(serviceException: ServiceException) =
        "${serviceException.errorCode} - ${serviceException.errorMessage}"

    private fun getErrorText(throwable: Throwable) =
        throwable.message ?: requireContext().getString(R.string.error_without_message)

    open fun manageServiceException(serviceException: ServiceException) {
        Toast.makeText(requireContext(), getErrorText(serviceException), Toast.LENGTH_LONG).show()
    }

    open fun manageException(throwable: Throwable) {
        Toast.makeText(requireContext(), getErrorText(throwable), Toast.LENGTH_LONG).show()
    }

    open fun bindView() {
        initBaseObserver()
    }

    open fun onCreated(savedInstanceState: Bundle?) {}
    open fun updateToolbar() {}
}
