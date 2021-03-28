package com.corgrimm.goodhusband.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.corgrimm.goodhusband.AppGlobalReceiver
import com.corgrimm.goodhusband.MainActivity
import com.corgrimm.goodhusband.R
import com.corgrimm.goodhusband.models.Reminder

object NotificationHelper {

    private const val ADMINISTER_REQUEST_CODE = 2019
    const val ACTION_NOTIFY_REMINDER = "com.corgrimm.goodhusband.action.REMINDER"

    /**
     * Sets up the notification channels for API 26+.
     * Note: This uses package name + channel name to create unique channelId's.
     *
     * @param context     application context
     * @param importance  importance level for the notificaiton channel
     * @param showBadge   whether the channel should have a notification badge
     * @param name        name for the notification channel
     * @param description description for the notification channel
     */
    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // Register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Helps issue the default application channels (package name + app name) notifications.
     * Note: this shows the use of [NotificationCompat.BigTextStyle] for expanded notifications.
     *
     * @param context    current application context
     * @param title      title for the notification
     * @param message    content text for the notification when it's not expanded
     * @param bigText    long form text for the expanded notification
     * @param autoCancel `true` or `false` for auto cancelling a notification.
     * if this is true, a [PendingIntent] is attached to the notification to
     * open the application.
     */
    fun createSampleDataNotification(context: Context, title: String, message: String,
                                     bigText: String, autoCancel: Boolean) {

        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_flowers)
            setContentTitle(title)
            setContentText(message)
            setAutoCancel(autoCancel)
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(autoCancel)

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }

    /**
     * Creates a notification for [ReminderData] with a full notification tap and a single action.
     *
     * @param context      current application context
     * @param reminderData ReminderData for this notification
     */

    fun createNotificationForPet(context: Context, reminderData: Reminder) {

        // create a group notification
        val groupBuilder = buildGroupNotification(context, reminderData)

        // create the pet notification
        val notificationBuilder = buildNotificationForPet(context, reminderData)

        // add an action to the pet notification
        val administerPendingIntent = createPendingIntentForAction(context, reminderData)
        notificationBuilder.addAction(R.drawable.ic_baseline_done_48, context.getString(R.string.reserve), administerPendingIntent)

        // call notify for both the group and the pet notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(reminderData.type.ordinal, groupBuilder.build())
        notificationManager.notify(reminderData.id, notificationBuilder.build())
    }

    /**
     * Builds and returns the [NotificationCompat.Builder] for the group notification for a pet type.
     *
     * @param context current application context
     * @param reminderData ReminderData for this notification
     */
    private fun buildGroupNotification(context: Context, reminderData: Reminder): NotificationCompat.Builder {
        val channelId = "${context.packageName}-${reminderData.type.name}"
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_flowers)
            setContentTitle(reminderData.type.name)
            setContentText(context.getString(R.string.group_notification_for, reminderData.type.name))
            setStyle(NotificationCompat.BigTextStyle().bigText(context.getString(R.string.group_notification_for, reminderData.type.name)))
            setAutoCancel(true)
            setGroupSummary(true)
            setGroup(reminderData.type.name)
        }
    }

    /**
     * Builds and returns the NotificationCompat.Builder for the Pet notification.
     *
     * @param context current application context
     * @param reminderData ReminderData for this notification
     */
    private fun buildNotificationForPet(context: Context, reminderData: Reminder): NotificationCompat.Builder {


        val channelId = "${context.packageName}-${reminderData.type.name}"

        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_flowers)
            setContentTitle(reminderData.name)
            setAutoCancel(true)

            // get a drawable reference for the LargeIcon
            val drawable = when (reminderData.type) {
                Reminder.ReminderType.Birthday -> R.drawable.ic_cake
                Reminder.ReminderType.Anniversary -> R.drawable.ic_champagne
                else -> R.drawable.ic_flowers
            }
            setLargeIcon(BitmapFactory.decodeResource(context.resources, drawable))
            setContentText("${reminderData.name}, ${reminderData.date}")
            setGroup(reminderData.type.name)

            // note is not important so if it doesn't exist no big deal
            if (reminderData.note != null) {
                setStyle(NotificationCompat.BigTextStyle().bigText(reminderData.note))
            }

            // Launches the app to open the reminder edit screen when tapping the whole notification
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                putExtra(ReminderDialog.KEY_ID, reminderData.id)
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }
    }

    /**
     * Creates the pending intent for the Administered Action for the pet notification.
     *
     * @param context current application context
     * @param reminderData ReminderData for this notification
     */
    private fun createPendingIntentForAction(context: Context, reminderData: Reminder): PendingIntent? {
        /*
            Create an Intent to update the ReminderData if Administer action is clicked
         */
        val administerIntent = Intent(context, AppGlobalReceiver::class.java).apply {
            action = NotificationHelper.ACTION_NOTIFY_REMINDER
            putExtra(AppGlobalReceiver.NOTIFICATION_ID, reminderData.id)
//            putExtra(ReminderDialog.KEY_ID, reminderData.id)
//            putExtra(ReminderDialog.KEY_ADMINISTERED, true)
        }

        return PendingIntent.getBroadcast(context, ADMINISTER_REQUEST_CODE, administerIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}