package com.example.smartexpensetracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartexpensetracker.data.dao.ExpenseDao
import com.example.smartexpensetracker.data.model.Expense

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            // This ensures the database is saved to a file named "expense_database"
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_tracker_db" // Unique name for the disk file
                )
                    .fallbackToDestructiveMigration() // Helps if you change the model later
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}