package com.corgrimm.goodhusband.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders_table ORDER BY name ASC")
    fun getAlphabetizedReminders(): Flow<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reminder: Reminder)

    @Query("DELETE FROM reminders_table")
    suspend fun deleteAll()

}