package com.pmdb.android.ui.main.home.add_movie_dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pmdb.android.R
import com.pmdb.android.data.model.api.MatchedMovieCompact
import com.pmdb.android.databinding.ItemMatchedMovieBinding
import com.pmdb.android.ui.base.BaseViewHolder

class MatchedMoviesAdapter(private val items: ArrayList<MatchedMovieCompact>) :
  RecyclerView.Adapter<MatchedMoviesAdapter.MatchedMovieViewHolder>() {

  private lateinit var listener: Listener

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchedMovieViewHolder {
    val binding =
      ItemMatchedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return MatchedMovieViewHolder(binding)
  }

  override fun onBindViewHolder(holder: MatchedMovieViewHolder, position: Int) {
    holder.onBind(items[position], position)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  fun setListener(listener: Listener) {
    this.listener = listener
  }

  fun clearItems() {
    items.clear()
  }

  fun addItems(movies: List<MatchedMovieCompact>) {
    items.addAll(movies)
    notifyDataSetChanged()
  }

  interface Listener {

    /**
     * انتخاب فیلم موردنظر از لیست
     *
     * @param movie فیلم انتخاب شده
     * @param itemPosition شماره ردیف فیلم انتخاب شده
     */
    fun onMovieSelect(movie: MatchedMovieCompact, itemPosition: Int)
  }

  inner class MatchedMovieViewHolder(private val binding: ItemMatchedMovieBinding) :
    BaseViewHolder<MatchedMovieCompact>(binding.root) {

    private lateinit var itemViewModel: MatchedMovieItemViewModel

    override fun onBind(item: MatchedMovieCompact, position: Int) {
      with(binding) {
        // پوستر پیش فرض هر فیلم
        imgPoster.setImageResource(R.drawable.ic_foreground)
        // عنوان بصورت پیش فرض انتخاب شده نباشد (برای نمایش کامل عنوان)
        txtTitle.isSelected = false

        // انتخاب فیلم موردنظر
        root.setOnClickListener { listener.onMovieSelect(item, position) }
        // با کلیک روی عنوان فیلم، متن کامل نمایش داده شود
        txtTitle.setOnClickListener { it.isSelected = true }

        itemViewModel = MatchedMovieItemViewModel(item)
        viewModel = itemViewModel
        executePendingBindings()
      }
    }

    fun showLoading(itemPosition: Int) {
      items[itemPosition].loading = true
      itemViewModel.loading.set(true)
    }

    fun hideLoading(itemPosition: Int) {
      items[itemPosition].loading = false
      itemViewModel.loading.set(false)
    }
  }
}