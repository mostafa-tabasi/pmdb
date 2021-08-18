package com.mstf.pmdb.ui.main.archive.archive_item_summary_dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.DialogArchiveItemSummaryBinding
import com.mstf.pmdb.ui.base.BaseBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArchiveItemSummaryDialog :
  BaseBottomSheetDialog<DialogArchiveItemSummaryBinding, ArchiveItemSummaryViewModel>(),
  ArchiveItemSummaryNavigator {

  override val viewModel: ArchiveItemSummaryViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.dialog_archive_item_summary
  private val args: ArchiveItemSummaryDialogArgs by navArgs()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setNavigator(this)
    setUp()
  }

  private fun setUp() {
    viewModel.fetchArchiveItem(args.archiveItemId)
  }
}