package com.corgrimm.goodhusband.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

  private val TAG = AlarmReceiver::class.java.simpleName

  override fun onReceive(context: Context?, intent: Intent?) {
    Log.d(TAG, "onReceive() called with: context = [$context], intent = [$intent]")
    if (context != null && intent != null && intent.action != null) {
      if (intent.action!!.equals(NotificationHelper.ACTION_NOTIFY_REMINDER, ignoreCase = true)) {
        if (intent.extras != null) {
//`          val reminderData = DataUtils.getReminderById(intent.extras!!.getInt(ReminderDialog.KEY_ID))
//          if (reminderData != null) {
//            NotificationHelper.createNotificationForPet(context, reminderData)
//          }`
        }
      }
    }
  }
}