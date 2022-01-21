package com.pmdb.android.ui.main.settings.about_dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.pmdb.android.BR
import com.pmdb.android.R
import com.pmdb.android.databinding.DialogAboutBinding
import com.pmdb.android.ui.base.BaseBottomSheetDialog
import com.pmdb.android.utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutDialog : BaseBottomSheetDialog<DialogAboutBinding, AboutViewModel>(), AboutNavigator {

  override val viewModel: AboutViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.dialog_about

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setNavigator(this)
    setUp()
  }

  private fun setUp() {
    viewDataBinding?.let {
      it.email.setOnClickListener { openEmailIntent() }
      it.linkedin.setOnClickListener { openLinkedinPage() }
    }
  }

  /**
   * هدایت کاربر جهت ارسال ایمیل
   */
  private fun openEmailIntent() {
    try {
      val selectorIntent = Intent(Intent.ACTION_SENDTO)
      selectorIntent.data = Uri.parse("mailto:")

      val emailIntent = Intent(Intent.ACTION_SEND)
      emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_mail)))
      emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
      emailIntent.selector = selectorIntent

      startActivity(Intent.createChooser(emailIntent, getString(R.string.mail_chooser_title)))
    } catch (e: Exception) {
      requireContext().toast(R.string.mail_app_not_found)
    }
  }

  /**
   * هدایت کاربر به صفحه ی linkedin
   */
  private fun openLinkedinPage() {
    val linkedinIntent =
      Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.developer_linkedin)))
    startActivity(linkedinIntent)
  }
}