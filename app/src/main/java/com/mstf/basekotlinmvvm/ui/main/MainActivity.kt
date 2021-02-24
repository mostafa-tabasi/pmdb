package com.mstf.basekotlinmvvm.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mstf.basekotlinmvvm.BR
import com.mstf.basekotlinmvvm.R
import com.mstf.basekotlinmvvm.databinding.ActivityMainBinding
import com.mstf.basekotlinmvvm.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {

  override val viewModel: MainViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.activity_main

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
    setUpNavigation()
  }

  private fun setUpNavigation() {
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    NavigationUI.setupWithNavController(
      viewDataBinding!!.bottomNavigation,
      navHostFragment.navController
    )
    navHostFragment.navController.addOnDestinationChangedListener(viewModel)
  }
}