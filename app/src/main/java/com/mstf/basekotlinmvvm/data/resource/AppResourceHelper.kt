package com.mstf.basekotlinmvvm.data.resource

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppResourceHelper @Inject constructor(private val context: Context) : ResourceHelper {

  override fun getString(resourceId: Int): String = context.getString(resourceId)

}