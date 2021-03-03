package com.mstf.pmdb.ui.sample.fragment

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.FragmentSampleBinding
import com.mstf.pmdb.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleFragment : BaseFragment<FragmentSampleBinding, SampleFragmentViewModel?>(),
  SampleFragmentNavigator {

  override val viewModel: SampleFragmentViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_sample

  override fun goBack() {
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
  }

  companion object {
    const val TAG = "SampleFragment"
    fun newInstance(): SampleFragment {
      val args = Bundle()
      val fragment = SampleFragment()
      fragment.arguments = args
      return fragment
    }
  }
}