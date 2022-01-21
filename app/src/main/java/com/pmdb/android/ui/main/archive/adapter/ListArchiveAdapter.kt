package com.pmdb.android.ui.main.archive.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pmdb.android.data.model.db.MovieEntity
import com.pmdb.android.databinding.ItemArchiveListBinding
import com.pmdb.android.ui.base.BaseViewHolder
import com.pmdb.android.ui.main.archive.ArchiveListener
import com.pmdb.android.utils.enums.RatingSite


class ListArchiveAdapter :
  PagedListAdapter<MovieEntity, ListArchiveAdapter.ArchiveViewHolder>(DIFF_CALLBACK) {

  private var listener: ArchiveListener? = null

  private var ratingSite: RatingSite? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewHolder {
    val binding = ItemArchiveListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
    return ArchiveViewHolder(binding)
  }

  override fun onBindViewHolder(archiveViewHolder: ArchiveViewHolder, position: Int) {
    getItem(position)?.let { archiveViewHolder.onBind(it, position) }
  }

  fun setListener(listener: ArchiveListener) {
    this.listener = listener
  }

  /**
   * ست کردن سایت موردنظری که امتیاز دادن براساس آن باید باشه
   *
   * @param site سایت موردنظر جهت نمایش امتیاز های آن
   */
  fun setRatingSite(site: RatingSite) {
    this.ratingSite = site
  }

  inner class ArchiveViewHolder(private val binding: ItemArchiveListBinding) :
    BaseViewHolder<MovieEntity>(binding.root) {

    override fun onBind(item: MovieEntity, position: Int) {
      with(item) {
        val viewModel = ArchiveItemViewModel(this)
        ratingSite?.let { viewModel.setRatingSite(it) }
        binding.viewModel = viewModel
        binding.parent.setOnClickListener { listener?.onMovieTapped(item.id!!) }
        binding.executePendingBindings()
      }
    }
  }

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieEntity>() {

      override fun areItemsTheSame(oldMovie: MovieEntity, newMovie: MovieEntity) =
        oldMovie.id == newMovie.id

      override fun areContentsTheSame(oldMovie: MovieEntity, newMovie: MovieEntity) =
        oldMovie == newMovie
    }
  }
}