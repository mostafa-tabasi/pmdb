package com.mstf.pmdb.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mstf.pmdb.BuildConfig
import com.mstf.pmdb.data.AppDataManager
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.data.local.db.AppDatabase
import com.mstf.pmdb.data.local.db.AppDbHelper
import com.mstf.pmdb.data.local.db.DbHelper
import com.mstf.pmdb.data.local.prefs.AppPreferencesHelper
import com.mstf.pmdb.data.local.prefs.PreferencesHelper
import com.mstf.pmdb.data.remote.ApiHeader.ProtectedApiHeader
import com.mstf.pmdb.data.remote.ApiHelper
import com.mstf.pmdb.data.remote.AppApiHelper
import com.mstf.pmdb.data.remote.OmdbApiClient
import com.mstf.pmdb.data.resource.AppResourceHelper
import com.mstf.pmdb.data.resource.ResourceHelper
import com.mstf.pmdb.di.annotation.ApiInfo
import com.mstf.pmdb.di.annotation.DatabaseInfo
import com.mstf.pmdb.di.annotation.PreferenceInfo
import com.mstf.pmdb.utils.AppConstants
import com.mstf.pmdb.utils.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton
  @Provides
  fun provideOkHttpClient(
    networkConnectionInterceptor: NetworkConnectionInterceptor
  ): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(networkConnectionInterceptor)
    .build()

  @Singleton
  @Provides
  fun provideOmdbApiClient(
    gson: Gson,
    okHttpclient: OkHttpClient
  ): OmdbApiClient = Retrofit.Builder()
    .client(okHttpclient)
    .baseUrl(BuildConfig.BASE_URL_OMDB)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
    .create(OmdbApiClient::class.java)

  @Provides
  @Singleton
  fun provideApiHelper(appApiHelper: AppApiHelper): ApiHelper = appApiHelper

  @Provides
  @ApiInfo
  fun provideOmdbApiKey(): String = BuildConfig.API_KEY_OMDB

  @Provides
  @Singleton
  fun provideAppDatabase(
    @DatabaseInfo dbName: String,
    @ApplicationContext context: Context
  ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, dbName)
    //TODO: migrations
    .build()

  @Provides
  @Singleton
  fun provideContext(application: Application): Context = application

  @Provides
  @Singleton
  fun provideDataManager(appDataManager: AppDataManager): DataManager = appDataManager

  @Provides
  @DatabaseInfo
  fun provideDatabaseName(): String = AppConstants.DB_NAME

  @Provides
  @Singleton
  fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper = appDbHelper

  @Provides
  @Singleton
  fun provideResourceHelper(appResourceHelper: AppResourceHelper): ResourceHelper =
    appResourceHelper

  @Provides
  @Singleton
  fun provideGson(): Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

  @Provides
  @PreferenceInfo
  fun providePreferenceName(): String = AppConstants.PREF_NAME

  @Provides
  @Singleton
  fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper =
    appPreferencesHelper

  @Provides
  @Singleton
  fun provideProtectedApiHeader(
    @ApiInfo apiKey: String,
    preferencesHelper: PreferencesHelper
  ): ProtectedApiHeader = ProtectedApiHeader(
    apiKey,
    preferencesHelper.currentUserId,
    preferencesHelper.accessToken
  )
}