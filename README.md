# Flight Search

Search airports from two pre-populated SQLite databases using **Room** while watching the search result in **real-time using Kotlin State Flows**. The last search query is stored using **Preferences DataStore**. The UI is made using **Jetpack Compose**.

This app is **fully tested**. Besides, the instrumentation tests are done using **Hilt and coroutine rules**.

This project follows modern practices. The guidance is given by the 
[Project: Create a flight search app](https://developer.android.com/codelabs/basic-android-kotlin-compose-flight-search?hl=es-419&continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-6-pathway-3%3Fhl%3Des-419%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-flight-search#0)
codelab.

## Main Tools
- Jetpack Compose for UI building
- Lifecycle-aware components such as `collectAsStateWithLifecycle()` API and the View Model.
- Model-View-View Model UI design pattern to separate UI state logic from UI, and the whole UI layer from the data layer.
- Preferences Datastore to persist simple key-value pairs in the internal file storage.
- Room to persist SQLite database.
- Repository pattern in the data layer to separate the data sources (Preferences Datastore and Room DAO and DB) from the repositories, whose main task consist in providing the necessary data to the rest of the app layers.
- Kotlin Coroutines to make one-shot calls to the data layer when hitting the search button.
- Kotlin Flows to monitor the data changes continuously while typing the search string.

#AndroidBasics

## Demo

![flight_search_demo](https://github.com/Camilo-Hernandez/Flight-Search/assets/36543483/49635464-1a8f-4ca2-be7b-b1417dd56973)
