package com.pmdb.android.utils.enums

enum class RatingSite {
  WHICHEVER_IS_AVAILABLE {
    override val id = 1
    override fun toString() = "Whichever is available"
  },
  IMDB {
    override val id = 2
    override fun toString() = "IMDb"
  },
  ROTTEN_TOMATOES {
    override val id = 3
    override fun toString() = "Rotten Tomatoes"
  },
  METACRITIC {
    override val id = 4
    override fun toString() = "Metacritic"
  };

  abstract val id: Int

  companion object {
    fun withId(id: Int): RatingSite? {
      return when (id) {
        WHICHEVER_IS_AVAILABLE.id -> WHICHEVER_IS_AVAILABLE
        IMDB.id -> IMDB
        ROTTEN_TOMATOES.id -> ROTTEN_TOMATOES
        METACRITIC.id -> METACRITIC
        else -> null
      }
    }
  }
}