package io.patcody.flicklist

data class MovieResult (
    val name: String,
    val id: String,
    val posterUrl: String,
    var inList: Boolean
)