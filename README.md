Inventory app
==================================

Solution code for Android Basics with Compose.

Introduction
------------

This app is an Inventory tracking app. Demos how to add, update, sell, and delete items from the local database.
This app demonstrated the use of Android Jetpack component [Room](https://developer.android.com/training/data-storage/room) database.
The app also leverages [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel),
[Flow](https://developer.android.com/kotlin/flow),
and [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/).

Pre-requisites
--------------

You need to know:
- How to create and use composables.
- How to navigate between composables, and pass data between them.
- How to use architecture components including ViewModel, Flow, StateFlow and StateUi.
- How to use coroutines for long-running tasks.
- SQLite database and the SQLite query language


Summary
--------------
https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-6-pathway-2%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-persisting-data-room#11


-Define your tables as data classes annotated with @Entity. 
-Define properties annotated with @ColumnInfo as columns in the tables.
    - Define a data access object (DAO) as an interface annotated with @Dao. The DAO maps Kotlin functions to database queries.
    - Use annotations to define @Insert, @Delete, and @Update functions.
    - Use the @Query annotation with an SQLite query string as a parameter for any other queries.
    - Use Database Inspector to view the data saved in the Android SQLite database.

