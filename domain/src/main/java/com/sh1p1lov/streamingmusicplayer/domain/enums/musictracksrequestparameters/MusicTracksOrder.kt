package com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters

enum class MusicTracksOrder(val value: String) {
    RELEVANCE("relevance"),
    DOWNLOADS_WEEK("downloads_week"),
    DOWNLOADS_MONTH("downloads_month"),
    DOWNLOADS_TOTAL("downloads_total"),
    LISTENS_WEEK("listens_week"),
    LISTENS_MONTH("listens_month"),
    LISTENS_TOTAL("listens_total"),
    POPULARITY_WEEK("popularity_week"),
    POPULARITY_MONTH("popularity_month"),
    POPULARITY_TOTAL("popularity_total"),
    NAME("name"),
    ALBUM_NAME("album_name"),
    ARTIST_NAME("artist_name"),
    RELEASE_DATE("releasedate"),
    DURATION("duration"),
    ID("id")
}