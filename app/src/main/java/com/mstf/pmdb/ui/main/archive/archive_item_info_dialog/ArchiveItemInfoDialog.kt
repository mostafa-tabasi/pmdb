package com.mstf.pmdb.ui.main.archive.archive_item_info_dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.DialogArchiveItemInfoBinding
import com.mstf.pmdb.ui.base.BaseBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArchiveItemInfoDialog :
  BaseBottomSheetDialog<DialogArchiveItemInfoBinding, ArchiveItemInfoViewModel>(),
  ArchiveItemInfoNavigator {

  override val viewModel: ArchiveItemInfoViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.dialog_archive_item_info
  private val args: ArchiveItemInfoDialogArgs by navArgs()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setNavigator(this)
    setUp()
  }

  private fun setUp() {
    viewModel.fetchArchiveItem(args.archiveItemId)
  }
}