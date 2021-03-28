package com.corgrimm.goodhusband

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.corgrimm.goodhusband.models.Reminder
import com.corgrimm.goodhusband.models.ReminderDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KParameter

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Reminder::class), version = 1, exportSchema = false)
@TypeConverters(Reminder.Converter::class)
public abstract class ReminderRoomDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    private class ReminderDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.reminderDao())
                }
            }
        }

        suspend fun populateDatabase(reminderDao: ReminderDao) {
            // Delete all content here.
            reminderDao.deleteAll()

            // Add sample words.
            var reminder = Reminder(1, "Sample 1", "Sept 1, 2000", Reminder.ReminderType.Birthday, 0, false, "")
            reminderDao.insert(reminder)
            reminder = Reminder(2, "Sample 2", "Nov 13, 2000", Reminder.ReminderType.Random, 10, true, "")
            reminderDao.insert(reminder)

            // TODO: Add your own words!
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ReminderRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ReminderRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderRoomDatabase::class.java,
                    "gh_database"
                )
                    .addCallback(ReminderDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}