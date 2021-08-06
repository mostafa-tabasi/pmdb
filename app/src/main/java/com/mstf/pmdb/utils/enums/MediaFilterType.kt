package com.mstf.pmdb.utils.enums

enum class MediaFilterType {
  MOVIES {
    override val id = 1
    override fun toString() = "Movies"
  },
  SERIES {
    override val id = 2
    override fun toString() = "Series"
  },
  BOTH {
    override val id = 3
    override fun toString() = "Both"
  };

  abstract val id: Int

  companion object {
    fun withId(id: Int): MediaFilterType? {
      return when (id) {
        MOVIES.id -> MOVIES
        SERIES.id -> SERIES
        BOTH.id -> BOTH
        else -> null
      }
    }
  }
}