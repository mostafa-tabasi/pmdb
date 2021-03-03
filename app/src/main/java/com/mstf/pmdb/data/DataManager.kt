package com.mstf.pmdb.data

import com.mstf.pmdb.data.local.db.DbHelper
import com.mstf.pmdb.data.local.prefs.PreferencesHelper
import com.mstf.pmdb.data.remote.ApiHelper
import com.mstf.pmdb.data.resource.ResourceHelper

interface DataManager : DbHelper, PreferencesHelper, ApiHelper, ResourceHelper {
}