package com.pmdb.android.utils.enums

enum class ArchiveDisplayType {
  LIST {
    override val id = 1
    override fun toString() = "List"
  },
  TILE {
    override val id = 2
    override fun toString() = "Tile"
  };

  abstract val id: Int

  companion object {
    fun withId(id: Int): ArchiveDisplayType? {
      return when (id) {
        LIST.id -> LIST
        TILE.id -> TILE
        else -> null
      }
    }
  }
}