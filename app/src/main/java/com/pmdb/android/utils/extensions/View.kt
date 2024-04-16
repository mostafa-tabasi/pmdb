package com.pmdb.android.utils.extensions

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.View.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar


fun View.visible() {
  visibility = VISIBLE
}

fun View.invisible() {
  visibility = INVISIBLE
}

fun View.gone() {
  visibility = GONE
}

fun View.isVisible(): Boolean {
  return visibility == VISIBLE
}

fun View.isInvisible(): Boolean {
  return visibility == INVISIBLE
}

fun View.isGone(): Boolean {
  return visibility == GONE
}

fun View.isInvisibleOrGone(): Boolean {
  return visibility == GONE || visibility == INVISIBLE
}

fun View.snackBar(
  message: String,
  duration: Int = Snackbar.LENGTH_SHORT,
  functionsToApply: (() -> Unit)? = null
) {
  Snackbar.make(this, message, duration).apply {
    functionsToApply?.invoke()
  }.show()
}

/**
 * تغییر آلفا ویو همراه با انیمیشن
 *
 * @param from مقدار آلفای ویو در شروع انیمیشن
 * @param to مقدار آلفای ویو در پایان انیمیشن
 * @param duration مدت زمان انیمیشن
 * @param beforeAnimate عملیاتی که قبل از شروع انیمیشن لازم است انجام شود
 * @param afterAnimate عملیاتی که بعد از اتمام انیمیشن لازم است انجام شود
 */
fun View.animateAlpha(
  from: Float,
  to: Float,
  duration: Long,
  beforeAnimate: (() -> Unit)? = null,
  afterAnimate: (() -> Unit)? = null
) {
  ValueAnimator.ofFloat(from, to).apply {
    this.duration = duration
    this.addUpdateListener { alpha = it.animatedValue as Float }
    this.addListener(object : Animator.AnimatorListener {
      override fun onAnimationStart(p0: Animator) {
        beforeAnimate?.invoke()
      }

      override fun onAnimationEnd(p0: Animator) {}
      override fun onAnimationCancel(p0: Animator) {}
      override fun onAnimationRepeat(p0: Animator) {}
    })
  }.start()
}

@BindingAdapter("android:layout_weight")
fun View.setWeight(weight: Float) {
  val params = layoutParams
  (params as LinearLayout.LayoutParams).weight = weight
  layoutParams = params
}

@BindingAdapter("android:layout_width")
fun View.setWidth(isWrapContent: Boolean) {
  val params = layoutParams
  params.width = if (isWrapContent) WRAP_CONTENT else 0
  (params as LinearLayout.LayoutParams).weight = if (isWrapContent) 0F else 1F
  layoutParams = params
}

/**
 * تغییر elevation ویو همراه با انیمیشن
 *
 * @param from مقدار آلفای ویو در شروع انیمیشن
 * @param to مقدار آلفای ویو در پایان انیمیشن
 * @param duration مدت زمان انیمیشن
 */
fun View.animateElevation(from: Float, to: Float, duration: Long) {
  ValueAnimator.ofFloat(from, to).apply {
    this.duration = duration
    this.addUpdateListener { elevation = it.animatedValue as Float }
  }.start()
}