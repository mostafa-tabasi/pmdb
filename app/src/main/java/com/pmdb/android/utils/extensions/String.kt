package com.pmdb.android.utils.extensions

fun String?.isNullOrEmptyAfterTrim(): Boolean {
  if (this == null) return true
  if (trim().isEmpty()) return true
  return false
}

fun String?.ifAvailable(): String {
  if (isNullOrEmptyAfterTrim()) return ""
  if (this == "N/A") return ""
  return this.toString()
}