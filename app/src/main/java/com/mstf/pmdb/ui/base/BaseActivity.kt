package com.mstf.pmdb.ui.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mstf.pmdb.utils.CommonUtils
import com.mstf.pmdb.utils.extensions.toast

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
  BaseNavigator {

  var viewDataBinding: T? = null
    private set
  abstract val viewModel: V?
  abstract val bindingVariable: Int

  @get:LayoutRes
  abstract val layoutId: Int
  private var progressDialog: ProgressDialog? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    performDataBinding()
  }

  @TargetApi(Build.VERSION_CODES.M)
  fun hasPermission(permission: String?): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
        checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
  }

  override fun hideKeyboard() {
    this.currentFocus?.let {
      val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
  }

  fun showLoading() {
    hideLoading()
    progressDialog = CommonUtils.showLoadingDialog(this)
  }

  fun hideLoading() {
    if (progressDialog != null && progressDialog!!.isShowing) {
      progressDialog!!.cancel()
    }
  }

  fun openActivityOnTokenExpire() {
    //TODO: redirect user to login activity
  }

  @TargetApi(Build.VERSION_CODES.M)
  fun requestPermissionsSafely(permissions: Array<String?>?, requestCode: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(permissions!!, requestCode)
    }
  }

  private fun performDataBinding() {
    viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
    viewDataBinding!!.setVariable(bindingVariable, viewModel)
    viewDataBinding!!.executePendingBindings()
  }

  override fun showError(message: String) = toast(message)
}