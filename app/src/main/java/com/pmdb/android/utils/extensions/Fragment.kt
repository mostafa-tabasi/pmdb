package com.pmdb.android.utils.extensions

import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
  view?.let { activity?.hideKeyboard(it) }
}

/**
 * فرگمنت موردنظر یکی از فرگمنت های موجود در لیست است یا خیر
 */
fun Fragment.isOneOfSpecifiedFragment(vararg fragmentClassList: Any): Boolean {
  fragmentClassList.forEach {
    if (this::class == it) return true
  }
  return false
}

fun Fragment.transparentStatusBar() {
  activity?.transparentStatusBar()
}

fun Fragment.coloredStatusBar(color: Int) {
  activity?.coloredStatusBar(color)
}

/*
fun Fragment.checkReadStoragePermissionFor(f: () -> Unit) {
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
    f.invoke()
    return
  }

  val context = activity!!
  if ((checkSelfPermission(
      context,
      Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_DENIED)
  ) {
    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    requestPermissions(permission, PERMISSION_CODE_READ_STORAGE)
  } else f.invoke()
}

fun Fragment.openDeviceGallery() {
  Intent(Intent.ACTION_PICK).apply {
    type = "image/*"
    action = Intent.ACTION_GET_CONTENT
    startActivityForResult(
      Intent.createChooser(this, "Select Picture"), REQUEST_CODE_SELECT_IMAGE
    )
  }
}
*/