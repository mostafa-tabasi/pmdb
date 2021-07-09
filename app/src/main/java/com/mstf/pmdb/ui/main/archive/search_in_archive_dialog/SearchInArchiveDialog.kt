package com.mstf.pmdb.ui.main.archive.search_in_archive_dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.DialogSearchInArchiveBinding
import com.mstf.pmdb.ui.base.BaseBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchInArchiveDialog :
  BaseBottomSheetDialog<DialogSearchInArchiveBinding, SearchInArchiveViewModel>(),
  SearchInArchiveNavigator {

  override val viewModel: SearchInArchiveViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.dialog_search_in_archive

  private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUp()
  }

  private fun setUp() {
    viewDataBinding?.let {
    }
    //setUpBottomSheet()
  }

  private fun setUpBottomSheet() {
    /*
    view?.let {
      it.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
          it.viewTreeObserver.removeOnGlobalLayoutListener(this)

          val dialog = dialog as BottomSheetDialog
          val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
          bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
          bottomSheet.layoutParams.height = MATCH_PARENT
          bottomSheetBehavior.peekHeight = viewDataBinding?.includeFindMovie?.root!!.height
          bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        }
      })
    }
    */
  }

  override fun onDestroyView() {
    view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
    super.onDestroyView()
  }
}