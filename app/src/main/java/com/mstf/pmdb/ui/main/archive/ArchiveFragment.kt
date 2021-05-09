package com.mstf.pmdb.ui.main.archive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.FragmentArchiveBinding
import com.mstf.pmdb.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveFragment : BaseFragment<FragmentArchiveBinding, ArchiveViewModel>(), ArchiveNavigator {

  override val viewModel: ArchiveViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_archive

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getMovies()
  }

  companion object {
    const val TAG = "ArchiveFragment"
    fun newInstance(): ArchiveFragment {
      val args = Bundle()
      val fragment = ArchiveFragment()
      fragment.arguments = args
      return fragment
    }
  }
}