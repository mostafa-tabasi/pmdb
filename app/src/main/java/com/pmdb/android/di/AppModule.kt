package com.pmdb.android.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pmdb.android.BuildConfig
import com.pmdb.android.data.AppDataManager
import com.pmdb.android.data.DataManager
import com.pmdb.android.data.local.db.AppDatabase
import com.pmdb.android.data.local.db.AppDbHelper
import com.pmdb.android.data.local.db.DbHelper
import com.pmdb.android.data.local.prefs.AppPreferencesHelper
import com.pmdb.android.data.local.prefs.PreferencesHelper
import com.pmdb.android.data.remote.ApiHeader.ProtectedApiHeader
import com.pmdb.android.data.remote.ApiHelper
import com.pmdb.android.data.remote.AppApiHelper
import com.pmdb.android.data.remote.OmdbApiClient
import com.pmdb.android.data.resource.AppResourceHelper
import com.pmdb.android.data.resource.ResourceHelper
import com.pmdb.android.di.annotation.ApiInfo
import com.pmdb.android.di.annotation.DatabaseInfo
import com.pmdb.android.ui.main.archive.adapter.ListArchiveAdapter
import com.pmdb.android.ui.main.archive.adapter.TilesArchiveAdapter
import com.pmdb.android.utils.AppConstants
import com.pmdb.android.utils.NetworkConnectionInterceptor
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
  fun provideAppUpdateManager(
    @ApplicationContext context: Context
  ): AppUpdateManager = AppUpdateManagerFactory.create(context)

  @Singleton
  @Provides
  fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

  @Singleton
  @Provides
  fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

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
  fun provideSharedPreferences(
    @ApplicationContext context: Context
  ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

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

  @Provides
  fun provideArchiveAdapter() = TilesArchiveAdapter()

  @Provides
  fun provideListArchiveAdapter() = ListArchiveAdapter()
}