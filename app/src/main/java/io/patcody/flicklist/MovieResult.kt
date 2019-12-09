package io.patcody.flicklist

import java.io.Serializable

data class MovieResult (
    val name: String,
    val id: String,
    val posterUrl: String,
    var inList: Boolean
) : Serializable {
    constructor() : this("", "", "", false)
}