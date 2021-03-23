package com.mstf.pmdb.data

import android.content.Context
import com.google.gson.Gson
import com.mstf.pmdb.data.local.db.DbHelper
import com.mstf.pmdb.data.local.prefs.PreferencesHelper
import com.mstf.pmdb.data.model.api.MatchedMovie
import com.mstf.pmdb.data.model.api.MatchedMovieList
import com.mstf.pmdb.data.remote.ApiHeader
import com.mstf.pmdb.data.remote.ApiHelper
import com.mstf.pmdb.data.resource.ResourceHelper
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(
  private val mContext: Context,
  private val dbHelper: DbHelper,
  private val preferencesHelper: PreferencesHelper,
  private val apiHelper: ApiHelper,
  private val resourceHelper: ResourceHelper,
  private val gson: Gson
) : DataManager {

  override var currentUserId: String?
    get() = preferencesHelper.currentUserId
    set(value) {
      preferencesHelper.currentUserId = value
      getApiHeader().protectedApiHeader.userId = value!!
    }

  override var accessToken: String?
    get() = preferencesHelper.accessToken
    set(accessToken) {
      preferencesHelper.accessToken = accessToken
      getApiHeader().protectedApiHeader.accessToken = accessToken
    }

  override fun getApiHeader(): ApiHeader = apiHelper.getApiHeader()

  override suspend fun findMovieByTitle(title: String): Response<MatchedMovieList> =
    apiHelper.findMovieByTitle(title)

  override suspend fun findMovieByImdbId(id: String): Response<MatchedMovie> =
    apiHelper.findMovieByImdbId(id)

  override fun getString(resourceId: Int) = resourceHelper.getString(resourceId)

  override fun getMovieGenreList(): List<String> = resourceHelper.getMovieGenreList()

  override fun getTvSeriesGenreList(): List<String> = resourceHelper.getTvSeriesGenreList()

  companion object {
    private const val TAG = "AppDataManager"
  }
}