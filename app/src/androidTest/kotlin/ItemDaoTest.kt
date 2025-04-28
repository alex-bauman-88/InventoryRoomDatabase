import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {
    private lateinit var itemDao: ItemDao
    private lateinit var inventoryDatabase: InventoryDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        itemDao = inventoryDatabase.itemDao()
    }
    // database created -> itemDao created -> via itemDao use .insert() and other ItemDao functions

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    private val item1 = Item(1, "Apples", 10.0, 20)
    private val item2 = Item(2, "Bananas", 15.0, 97)

    // utility function, not a test one
    private suspend fun addOneItemToDb() {
        itemDao.insert(item1)
    }

    // utility function, not a test one
    private suspend fun addTwoItemsToDb() {
        itemDao.insert(item1)
        itemDao.insert(item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDb() = runBlocking {
        addOneItemToDb()
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDb() = runBlocking {
        addTwoItemsToDb()
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDb() = runBlocking {
        addTwoItemsToDb()
        val item1Update = Item(1, "Apples", 15.0, 25)
        val item2Update = Item(2, "Bananas", 5.0, 50)
        itemDao.update(item1Update)
        itemDao.update(item2Update)
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1Update)
        assertEquals(allItems[1], item2Update)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllItemsFromDb() = runBlocking {
        addTwoItemsToDb()
        itemDao.delete(item1)
        itemDao.delete(item2)
        val allItems = itemDao.getAllItems().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDb() = runBlocking {
        addOneItemToDb()

       // Variant 2 – collect immediately and work with the concrete Item
        val item = itemDao.getItem(1).first()
        assertEquals(item, item1)


        /*Why Variant 2 is preferable for a simple “one-shot” assertion:
        1. Correct type in the variable • item is an Item, so the name matches its type. •
           In Variant 1 item is actually a Flow<Item>, which can be misleading unless you name
           it itemFlow or similar.
        2. No accidental re-collection • You call first() exactly once. • In Variant 1 every time
           you write item.first() you re-collect the flow and re-run the SQL query, which is
           unnecessary and could hurt performance if the flow is used multiple times.
        3. Less noise in the assertion • The assertion itself is simpler and reads like
           a value comparison.*/


        /**Variant 1 (from the lesson)

        val item = itemDao.getItem(1)  // item is Flow<Item>
        assertEquals(item.first(), item1) // first() collects the flow once and gives you Item

      What first() does:

      - Suspends until the flow emits its first value.
      - Cancels further collection and returns that value.

      In other words, .first() turns the Flow<Item> into a single Item, which is what assertEquals needs.
      If you changed your DAO signature to return Item directly e.g., then you could write:
      `val item = itemDao.getItem(1)
      assertEquals(item, item1)`
      — but as long as the DAO returns a Flow, you have to collect it, and first() is the simplest way
      to do so in a unit test.

       */
    }

}










