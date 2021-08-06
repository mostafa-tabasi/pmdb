package com.mstf.pmdb.ui.main.archive.search_in_archive_dialog

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.DialogSearchInArchiveBinding
import com.mstf.pmdb.ui.base.BaseBottomSheetDialog
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_DIRECTOR
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_DISPLAY_TYPE
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_FILTER_TYPE
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_FROM_YEAR
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_MOVIE_TITLE
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_TO_YEAR
import com.mstf.pmdb.utils.enums.ArchiveDisplayType
import com.mstf.pmdb.utils.enums.MediaFilterType
import dagger.hilt.android.AndroidEntryPoint
import net.cachapa.expandablelayout.ExpandableLayout


@AndroidEntryPoint
class SearchInArchiveDialog :
  BaseBottomSheetDialog<DialogSearchInArchiveBinding, SearchInArchiveViewModel>(),
  SearchInArchiveNavigator {

  override val viewModel: SearchInArchiveViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.dialog_search_in_archive
  private val args: SearchInArchiveDialogArgs by navArgs()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setNavigator(this)
    setUp()
  }

  private fun setUp() {
    viewModel.changeDisplayType(
      ArchiveDisplayType.withId(args.displayType) ?: ArchiveDisplayType.TILE
    )
    viewModel.changeFilterType(MediaFilterType.withId(args.filterType) ?: MediaFilterType.BOTH)
    viewModel.setTitle(args.title)
    viewModel.setFromYear(args.fromYear)
    viewModel.setToYear(args.toYear)
    viewModel.setDirector(args.director)

    viewDataBinding?.let {
      // ست کردن انیمیشن برای تغییرات لایه های موردنظر
      LayoutTransition().apply {
        setAnimateParentHierarchy(false)
        setDuration(250)
      }.also { transition -> it.layoutButtons.layoutTransition = transition }

      it.confirm.setOnClickListener {
        // برگشت اطلاعات موردنیاز به صفحه ی قبل
        setFragmentResult(
          REQUEST_KEY, bundleOf(
            BUNDLE_KEY_DISPLAY_TYPE to viewModel.displayType.value,
            BUNDLE_KEY_FILTER_TYPE to viewModel.filterType.value,
            BUNDLE_KEY_MOVIE_TITLE to viewModel.title.get(),
            BUNDLE_KEY_FROM_YEAR to viewModel.fromYear.get(),
            BUNDLE_KEY_TO_YEAR to viewModel.toYear.get(),
            BUNDLE_KEY_DIRECTOR to viewModel.director.get(),
          )
        )
        findNavController().navigateUp()
      }
      it.layoutDisplay.setOnClickListener { _ -> it.expandableDisplay.toggle() }
      it.layoutFilter.setOnClickListener { _ -> it.expandableFilter.toggle() }
      it.layoutTitle.setOnClickListener { _ -> toggleFilter(it.expandableTitle) }
      it.layoutYear.setOnClickListener { _ -> toggleFilter(it.expandableYear) }
      it.layoutDirector.setOnClickListener { _ -> toggleFilter(it.expandableDirector) }
    }
  }

  /**
   * تغییر وضعیت لایه ی موردنظر و بستن بقیه لایه های فیلتر
   *
   * @param layout لایه ی موردنظر جهت تغییر وضعیت
   */
  private fun toggleFilter(layout: ExpandableLayout) {
    viewDataBinding?.let {
      if (layout == it.expandableTitle) it.expandableTitle.toggle()
      else it.expandableTitle.collapse()
      if (layout == it.expandableYear) it.expandableYear.toggle()
      else it.expandableYear.collapse()
      if (layout == it.expandableDirector) it.expandableDirector.toggle()
      else it.expandableDirector.collapse()
    }
  }

  override fun onDestroyView() {
    view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
    super.onDestroyView()
  }

  companion object {
    const val REQUEST_KEY = "request_key_archive_search"
  }
}