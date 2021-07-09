package com.mstf.pmdb.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.FragmentHomeBinding
import com.mstf.pmdb.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), HomeNavigator {

  override val viewModel: HomeViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_home

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setNavigator(this)
  }

  /**
   * نمایش دیالوگ افزودن فیلم جدید
   */
  fun openAddMovieDialog() {
    val action = HomeFragmentDirections.actionHomeFragmentToAddMovieDialog()
    findNavController().navigate(action)
  }

  companion object {
    const val TAG = "HomeFragment"
    fun newInstance(): HomeFragment {
      val args = Bundle()
      val fragment = HomeFragment()
      fragment.arguments = args
      return fragment
    }
  }

}