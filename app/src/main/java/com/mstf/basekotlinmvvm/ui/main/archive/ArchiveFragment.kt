package com.mstf.basekotlinmvvm.ui.main.archive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mstf.basekotlinmvvm.BR
import com.mstf.basekotlinmvvm.R
import com.mstf.basekotlinmvvm.databinding.FragmentArchiveBinding
import com.mstf.basekotlinmvvm.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveFragment : BaseFragment<FragmentArchiveBinding, ArchiveViewModel>(), ArchiveNavigator {

  override val viewModel: ArchiveViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_archive

  companion object {
    const val TAG = "ArchiveFragment"
    fun newInstance(): ArchiveFragment {
      val args = Bundle()
      val fragment = ArchiveFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setNavigator(this)
  }
}