# GWU CSCI 4137 - Project 2
FlickList is an app designed to help you track what movies you want to see.
Using the IMDB API available on [RapidAPI](https://rapidapi.com/imdb/api/movie-database-imdb-alternative/), you can get details about each movie.
Accounts and list storage are handled with FireBase.

## Testing Locally
You need API keys from 2 sources-  RapidAPI, for the IMDB API, and FireBase, for accounts and database storage.

### RapidAPI
The API Key from RapidAPI should be a "string values" XML file stored in `app/src/debug/res/values/api_keys.xml` and `app/src/release/values/api_keys.xml`. The only value needed inside is `rapid_api_key`.

### FireBase
Firebase requires making a new project in the FireBase console. Once that has been made, you can download the configuration file, `google-services.json`. This should be stored at `app/google-services.json`.
