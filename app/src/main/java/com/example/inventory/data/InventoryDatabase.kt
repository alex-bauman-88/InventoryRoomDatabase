package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// database holder
/**
 * Database class with a singleton Instance object.
 *
 * Your ViewModels interact with the database via the DAO and provide data to the UI.
 */
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    /**The value of a volatile variable is never cached, and all reads and writes are to and from the main memory. These features help ensure the value of Instance is always up to date and is the same for all execution threads. It means that changes made by one thread to Instance are immediately visible to all other threads.*/
    companion object {

        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {

            /** Multiple threads can potentially ask for a database instance at the same time,
            which results in two databases instead of one. This issue is known as a race condition.
            Wrapping the code to get the database inside a synchronized block means that only one
            thread of execution at a time can enter this block of code, which makes sure
            the database only gets initialized once.
            Use synchronized{} block to avoid the race condition.*/

            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    InventoryDatabase::class.java,
                    "item_database"
                ).build().also { Instance = it }
            }
        }
    }
    /* You can use this code as a template for your future projects. The way you create
    the RoomDatabase instance is similar to the process in the previous steps.
    You might have to replace the entities and DAOs specific to your app.*/
}

