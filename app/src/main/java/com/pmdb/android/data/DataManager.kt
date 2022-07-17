package com.pmdb.android.data

import com.pmdb.android.data.firestore.FirebaseHelper
import com.pmdb.android.data.local.db.DbHelper
import com.pmdb.android.data.local.prefs.PreferencesHelper
import com.pmdb.android.data.remote.ApiHelper
import com.pmdb.android.data.resource.ResourceHelper

interface DataManager : DbHelper,
  PreferencesHelper,
  ApiHelper,
  FirebaseHelper,
  ResourceHelper