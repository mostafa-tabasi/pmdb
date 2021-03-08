package com.mstf.pmdb.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mstf.pmdb.utils.extensions.toast

abstract class BaseBottomSheetDialog<T : ViewDataBinding?, V : BaseViewModel<*>?> :
  BottomSheetDialogFragment(), BaseNavigator {

  var baseActivity: BaseActivity<*, *>? = null
    private set
  private var rootView: View? = null
  var viewDataBinding: T? = null
    private set
  abstract val viewModel: V?
  abstract val bindingVariable: Int

  @get:LayoutRes
  abstract val layoutId: Int

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(false)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
    rootView = viewDataBinding!!.root
    return rootView
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is BaseActivity<*, *>) baseActivity = context
  }

  override fun onDetach() {
    baseActivity = null
    super.onDetach()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewDataBinding!!.setVariable(bindingVariable, viewModel)
    viewDataBinding!!.lifecycleOwner = this
    viewDataBinding!!.executePendingBindings()
  }

  override fun hideKeyboard() {
    baseActivity?.hideKeyboard()
  }

  val isNetworkConnected: Boolean
    get() = baseActivity != null && baseActivity!!.isNetworkConnected

  fun openActivityOnTokenExpire() {
    baseActivity?.openActivityOnTokenExpire()
  }

  override fun showError(message: String) = requireContext().toast(message)
}