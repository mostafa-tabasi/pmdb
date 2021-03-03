package com.mstf.pmdb.ui.main.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.FragmentHomeBinding
import com.mstf.pmdb.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentHomeBinding, SettingsViewModel>(), SettingsNavigator {

  override val viewModel: SettingsViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_settings

  companion object {
    const val TAG = "SettingsFragment"
    fun newInstance(): SettingsFragment {
      val args = Bundle()
      val fragment = SettingsFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setNavigator(this)
  }

}