/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        // Instantiate the database instance by calling getDatabase() on the
        // InventoryDatabase class passing in the context and call .itemDao() to create
        // the instance of Dao.
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }
}

/* AppContainer is a simple service-locator / dependency-injection interface that exposes
the components your app needs—in this case an ItemsRepository.

AppDataContainer is its production implementation: you give it an Android Context,
and it lazily builds an OfflineItemsRepository backed by your Room InventoryDatabase.
You’d typically create one AppDataContainer e.g. in your Application or Activity and
use it to get the repository wherever you need data access.

In simple words:

Think of AppContainer as a “toolbox” that says “I have one thing inside: itemsRepository.”

It’s just a promise. AppDataContainer is the real toolbox you build when your app starts.
You give it the Android Context, and only when you first ask for itemsRepository it:
  -- Makes or finds the Room database InventoryDatabase.
 -- Pulls out the DAO itemDao.
 -- Wraps that in an OfflineItemsRepository object.

Why do it this way?
  -- Keeps all setup code in one spot.
  -- Makes it easy to swap in a fake repository for tests.
  -- Ensures you only build the database once, on demand.


Why container for repository?

Putting database setup in a container instead of inside the repository keeps
responsibilities clean and your code flexible:
• Single place for wiring. You can swap out or configure dependencies DB, network, fakes
  without touching repository code.
• One shared instance. The container ensures you only build the Room database once
  and reuse it everywhere.
• Easier testing. In tests you replace the container with one that gives a fake
  or in-memory repo, without changing repository logic.
• Clear separation. Repositories focus on data operations; containers handle how those repos
  get their dependencies.


Example: container for Retrofit
https://github.com/alex-bauman-88/Amphibians/blob/master/app/src/main/java/com/example/amphibians/data/AppContainer.kt


  */
