package com.mstf.pmdb.utils.enums

enum class MovieGenre {
  ACTION {
    override fun label() = "Action"
  },
  ADVENTURE {
    override fun label() = "Adventure"
  },
  ANIMATION {
    override fun label() = "Animation"
  },
  BIOGRAPHY {
    override fun label() = "Biography"
  },
  COMEDY {
    override fun label() = "Comedy"
  },
  CRIME {
    override fun label() = "Crime"
  },
  DOCUMENTARY {
    override fun label() = "Documentary"
  },
  DRAMA {
    override fun label() = "Drama"
  },
  FAMILY {
    override fun label() = "Family"
  },
  FANTASY {
    override fun label() = "Fantasy"
  },
  FILM_NOIR {
    override fun label() = "Film-Noir"
  },
  HISTORY {
    override fun label() = "History"
  },
  HORROR {
    override fun label() = "Horror"
  },
  MUSIC {
    override fun label() = "Music"
  },
  MUSICAL {
    override fun label() = "Musical"
  },
  MYSTERY {
    override fun label() = "Mystery"
  },
  ROMANCE {
    override fun label() = "Romance"
  },
  SCI_FI {
    override fun label() = "Sci-Fi"
  },
  SHORT {
    override fun label() = "Short"
  },
  SPORT {
    override fun label() = "Sport"
  },
  THRILLER {
    override fun label() = "Thriller"
  },
  WAR {
    override fun label() = "War"
  },
  WESTERN {
    override fun label() = "Western"
  };

  abstract fun label(): String

  fun labelWithSeparator() = "${label()},"

  companion object {

    fun list(): List<String> {
      val list = arrayListOf<String>()
      values().forEach { list.add(it.label()) }
      return list
    }

  }
}