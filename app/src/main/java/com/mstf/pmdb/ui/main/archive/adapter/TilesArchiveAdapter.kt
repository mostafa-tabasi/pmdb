package com.mstf.pmdb.ui.main.archive.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.mstf.pmdb.data.model.db.MovieEntity
import com.mstf.pmdb.databinding.ItemArchiveGridHorizontalBinding
import com.mstf.pmdb.ui.base.BaseViewHolder
import com.mstf.pmdb.ui.main.archive.ArchiveListener

class TilesArchiveAdapter :
  PagedListAdapter<MovieEntity, TilesArchiveAdapter.ArchiveViewHolder>(DIFF_CALLBACK) {

  private var listener: ArchiveListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewHolder {
    val binding =
      ItemArchiveGridHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
    return ArchiveViewHolder(binding)
  }

  override fun onBindViewHolder(archiveViewHolder: ArchiveViewHolder, position: Int) {
    getItem(position)?.let { archiveViewHolder.onBind(it, position) }
  }

  fun setListener(listener: ArchiveListener) {
    this.listener = listener
  }

  inner class ArchiveViewHolder(private val binding: ItemArchiveGridHorizontalBinding) :
    BaseViewHolder<MovieEntity>(binding.root) {

    override fun onBind(item: MovieEntity, position: Int) {
      with(item) {
        val viewModel = ArchiveItemViewModel(this)
        binding.viewModel = viewModel
        binding.parent.setOnClickListener {
          listener?.onMovieTapped(item.id!!)

          /*  TODO: handle multi select for next release
          viewModel.changeSelected(listener?.onMovieTapped(item.id!!))
          */
        }
        /*  TODO: handle multi select for next release
        binding.parent.setOnLongClickListener {
          viewModel.changeSelected(listener?.onMovieLongTouch(item.id!!))
          true
        }
        viewModel.isSelected.observe(binding.lifecycleOwner!!, { isSelected ->
          if (isSelected) (binding.root as CardView).animateElevation(0f, 32f, 100)
          else (binding.root as CardView).animateElevation(32f, 0f, 100)
        })
        */
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