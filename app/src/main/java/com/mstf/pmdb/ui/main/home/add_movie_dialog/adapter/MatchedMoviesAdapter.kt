package com.mstf.pmdb.ui.main.home.add_movie_dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstf.pmdb.R
import com.mstf.pmdb.data.model.api.MatchedMovieCompact
import com.mstf.pmdb.databinding.ItemMatchedMovieBinding
import com.mstf.pmdb.ui.base.BaseViewHolder

class MatchedMoviesAdapter(private val items: ArrayList<MatchedMovieCompact>) :
  RecyclerView.Adapter<BaseViewHolder>() {

  private lateinit var listener: Listener

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    val binding =
      ItemMatchedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return MatchedMovieViewHolder(binding)
  }

  override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
    holder.onBind(position)
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
    BaseViewHolder(binding.root) {

    private lateinit var itemViewModel: MatchedMovieItemViewModel

    override fun onBind(position: Int) {
      with(binding) {
        // پوستر پیش فرض هر فیلم
        imgPoster.setImageResource(R.drawable.ic_foreground)
        // عنوان بصورت پیش فرض انتخاب شده نباشد (برای نمایش کامل عنوان)
        txtTitle.isSelected = false

        val item = items[position]
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