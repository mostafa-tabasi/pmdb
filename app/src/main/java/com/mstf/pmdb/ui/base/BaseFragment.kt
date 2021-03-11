package com.mstf.pmdb.ui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mstf.pmdb.utils.extensions.snackBar

abstract class BaseFragment<T : ViewDataBinding?, V : BaseViewModel<*>?> : Fragment(),
  BaseNavigator {

  var baseActivity: BaseActivity<*, *>? = null
    private set
  private var rootView: View? = null
  private var viewDataBinding: T? = null
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
    view?.let {
      val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
  }

  val isNetworkConnected: Boolean
    get() = baseActivity != null && baseActivity!!.isNetworkConnected

  fun openActivityOnTokenExpire() {
    baseActivity?.openActivityOnTokenExpire()
  }

  override fun showError(message: String) = requireView().snackBar(message)
}