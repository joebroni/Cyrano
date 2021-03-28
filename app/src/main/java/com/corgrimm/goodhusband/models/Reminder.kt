package com.corgrimm.goodhusband.models

import android.renderscript.RenderScript
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "reminders_table")
class Reminder(@PrimaryKey(autoGenerate = true) val id: Int,
               val name: String,
               val date : String,
               var type : ReminderType,
               val frequency : Int, //in days
               val recurring : Boolean,
                var note : String) {

    enum class ReminderType {
        Birthday,
        Anniversary,
        Date,
        Random
    }

    class Converter {
        @TypeConverter
        fun fromReminderType(reminderType: ReminderType): String {
            return reminderType.name
        }

        @TypeConverter
        fun toReminderType(reminderType: String): ReminderType {
            return ReminderType.valueOf(reminderType)
        }
    }
}

