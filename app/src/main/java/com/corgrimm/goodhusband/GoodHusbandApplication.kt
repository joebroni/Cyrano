package com.corgrimm.goodhusband

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.corgrimm.goodhusband.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GoodHusbandApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { ReminderRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ReminderRepository(database.reminderDao()) }

    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createNotificationChannel(this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")
    }
}