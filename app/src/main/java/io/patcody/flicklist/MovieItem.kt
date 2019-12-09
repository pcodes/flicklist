package io.patcody.flicklist

import java.io.Serializable

data class MovieItem (
    val id: String,
    val movieTitle: String,
    val year: String,
    val rating: String,
    val releaseDate: String,
    val director: String,
    val plot: String,
    val imdbRating: String
) : Serializable {
    constructor() : this("", "", "", "", "", "", "", "")
}