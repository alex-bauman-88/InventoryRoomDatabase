package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    /**Now Room generates all the necessary code to insert the item into the database. When you call any of the DAO functions that are marked with Room annotations, Room executes the corresponding SQL query on the database.*/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item) // pass the item we want to delete

    /**There is no convenience annotation for the remaining functionality, so you have to use
     the @Query annotation and supply SQLite queries.

    Flow in Room database can keep the data up-to-date by emitting a notification
    whenever the data in the database changes. This allows you to observe the data
    and update your UI accordingly.*/
    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from item ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

}