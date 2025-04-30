package com.example.graminawas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.graminawas.data.dao.BeneficiaryDao
import com.example.graminawas.data.dao.ProjectDao
import com.example.graminawas.data.dao.ProjectDeadlineDao
import com.example.graminawas.data.dao.ProjectUpdateDao
import com.example.graminawas.data.entities.Beneficiary
import com.example.graminawas.data.entities.Project
import com.example.graminawas.data.entities.ProjectDeadline
import com.example.graminawas.data.entities.ProjectUpdate
import java.util.Date

@Database(
    entities = [
        Beneficiary::class,
        Project::class,
        ProjectDeadline::class,
        ProjectUpdate::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun beneficiaryDao(): BeneficiaryDao
    abstract fun projectDao(): ProjectDao
    abstract fun projectDeadlineDao(): ProjectDeadlineDao
    abstract fun projectUpdateDao(): ProjectUpdateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gramin_awas_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @androidx.room.TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @androidx.room.TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
