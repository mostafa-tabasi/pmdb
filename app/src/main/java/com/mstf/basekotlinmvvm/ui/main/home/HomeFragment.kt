package com.mstf.basekotlinmvvm.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mstf.basekotlinmvvm.BR
import com.mstf.basekotlinmvvm.R
import com.mstf.basekotlinmvvm.databinding.FragmentHomeBinding
import com.mstf.basekotlinmvvm.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), HomeNavigator {

  override val viewModel: HomeViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_home

  companion object {
    const val TAG = "HomeFragment"
    fun newInstance(): HomeFragment {
      val args = Bundle()
      val fragment = HomeFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setNavigator(this)
  }

}