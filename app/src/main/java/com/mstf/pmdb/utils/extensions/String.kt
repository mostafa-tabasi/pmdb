package com.mstf.pmdb.utils.extensions

fun String?.isNullOrEmptyAfterTrim(): Boolean {
  if (this == null) return true
  if (trim().isEmpty()) return true
  return false
}