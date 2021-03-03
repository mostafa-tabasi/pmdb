package com.mstf.pmdb.ui.sample.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.ActivitySampleBinding
import com.mstf.pmdb.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : BaseActivity<ActivitySampleBinding, SampleActivityViewModel>(),
  SampleActivityNavigator {

  override val viewModel: SampleActivityViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.activity_sample

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
  }

  companion object {
    private const val TAG = "SampleActivity"
    fun newIntent(context: Context) = Intent(context, SampleActivity::class.java)
  }
}