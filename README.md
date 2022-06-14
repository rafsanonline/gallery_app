


##Introduction

This is a simple app for fetching a list of photos from the Unplash server. They provide free API(s) for high-quality photos.
Link: https://unsplash.com/developers

## app structure:

I've set up a clean architecture with MVVM design pattern to develop this app with the latest android jetpack components.
The main components of the app are the following:

1. Jetpack Compose
2. View model
3. Hilt
4. Flow
5. Room Database
6. Paging

Jetpack compose is the latest UI toolkit provided by android which comes with no XML.

## testing

I've used Junit4 for performing the caching data store testing in the room database.
Google truth library has been used to evaluate the values of the test results.

