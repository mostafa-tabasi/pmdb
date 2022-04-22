package com.pmdb.android.utils.enums

enum class AppTheme {
  LIGHT {
    override val id = 1
    override fun toString() = "Light"
  },
  NIGHT {
    override val id = 2
    override fun toString() = "Dark"
  };

  abstract val id: Int

  companion object {
    fun withId(id: Int): AppTheme? {
      return when (id) {
        LIGHT.id -> LIGHT
        NIGHT.id -> NIGHT
        else -> null
      }
    }
  }
}